package arrow.meta.encoder.jvm

import arrow.common.messager.logW
import arrow.common.utils.removeBackticks
import arrow.meta.ast.PackageName
import arrow.meta.ast.TypeName
import arrow.meta.encoder.KotlinReservedKeywords

internal fun String.removeVariance(): String =
  replace("out ", "").replace("in ", "")

fun String.asPlatform(): String =
  removeBackticks()
    .replaceFirst("kotlin.collections.", "java.util.")
    .replaceFirst("kotlin.", "java.lang.")

fun String.asKotlin(): String =
  removeBackticks()
    .replace("/", ".")
    .replace("kotlin.jvm.functions", "kotlin")
    .replace("java.util.List", "kotlin.collections.List")
    .replace("java.util.Set", "kotlin.collections.Set")
    .replace("java.util.Map", "kotlin.collections.Map")
    .replace("java.util.SortedMap", "kotlin.collections.SortedMap")
    .replace("java.util.Collection", "kotlin.collections.Collection")
    .replace("java.lang.Throwable", "kotlin.Throwable").let {
      if (it == "java.lang") it.replace("java.lang", "kotlin")
      else it
    }.let {
      if (it == "java.util") it.replace("java.util", "kotlin.collections")
      else it
    }
    .replace("kotlin.Integer", "kotlin.Int")
    .replace("Integer", "Int")
    .replace("java.lang.String", "kotlin.String")

internal fun String.asClassy(): TypeName.Classy {
  val seed = toString().asKotlin()
  val rawTypeName = seed.substringBefore("<")
  val pckg = if (rawTypeName.contains(".")) rawTypeName.substringBeforeLast(".") else ""
  val simpleName = rawTypeName.substringAfterLast(".")
  return TypeName.Classy(simpleName, rawTypeName, PackageName(pckg))
}

private val kindRegex: Regex =
  "arrow.Kind<(.*), (.*)>".toRegex()

fun String.downKParts(): List<String> =
  when (val matchResult = kindRegex.find(this)) {
    null -> listOf(this)
    else -> {
      val witness = matchResult.groupValues[1]
      val value = matchResult.groupValues[2]
      witness.downKParts() + value.downKParts()
    }
  }

data class DownKindReduction(
  val pckg: String,
  val name: String,
  val addtitionalTypeArgs: List<String> = emptyList()
)

internal fun String.downKind(api: JvmMetaApi): DownKindReduction {
  val dkOld = downKindOld()
  val parts = downKParts()
  return if (parts.isEmpty()) {
    val pckg =
      if (this.contains(".")) this.substringBeforeLast(".")
      else ""
    DownKindReduction(pckg, this)
  } else {
    val dataType = parts[0]
    val pckg =
      if (dataType.contains(".")) dataType.substringBeforeLast(".")
      else ""
    val simpleName = dataType.substringAfterLast(".").substringBefore("<")
    val unAppliedName =
      if (simpleName.startsWith("For")) simpleName.drop("For".length)
      else simpleName
    when {
      unAppliedName.endsWith("PartialOf") ->
        DownKindReduction(pckg, unAppliedName.substringBeforeLast("PartialOf"), parts.drop(1))
      unAppliedName.endsWith("Of") ->
        DownKindReduction(pckg, unAppliedName.substringBeforeLast("Of"), parts.drop(1))
      else -> {
        if (unAppliedName == "StateT") {
          api.logW(unAppliedName + ", parts: " + parts)
        }
        DownKindReduction(pckg, unAppliedName, parts.drop(1))
      }
    }
  }
}

internal fun String.downKindOld(): Pair<String, String> =
  run {
    val classy = asClassy()
    val kindedClassy = when {
      classy.fqName == "arrow.Kind" -> {
        val result = substringAfter("arrow.Kind<").substringBefore(",").asClassy()
        if (result.fqName == "arrow.typeclasses.ForConst") result
        else classy
      }
      else -> classy
    }
    val unAppliedName =
      if (kindedClassy.simpleName.startsWith("For")) kindedClassy.simpleName.drop("For".length)
      else kindedClassy.simpleName
    when {
      unAppliedName.endsWith("PartialOf") -> Pair(kindedClassy.pckg.value, unAppliedName.substringBeforeLast("PartialOf"))
      unAppliedName.endsWith("Of") -> Pair(kindedClassy.pckg.value, unAppliedName.substringBeforeLast("Of"))
      else -> Pair(kindedClassy.pckg.value, unAppliedName)
    }
  }

fun String.quote(): String =
  split(".").joinToString(".") {
    if (KotlinReservedKeywords.contains(it)) "`$it`"
    else it
  }