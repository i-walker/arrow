package arrow.core.extensions

import arrow.core.typeclasses.Eq
import arrow.core.typeclasses.Hash
import arrow.core.typeclasses.Show

interface BooleanShow : Show<Boolean> {
  override fun Boolean.show(): String =
    this.toString()
}

interface BooleanEq : Eq<Boolean> {
  override fun Boolean.eqv(b: Boolean): Boolean = this == b
}

interface BooleanHash : Hash<Boolean>, BooleanEq {
  override fun Boolean.hash(): Int = this.hashCode()
}

fun Boolean.Companion.show(): Show<Boolean> =
  object : BooleanShow {}

fun Boolean.Companion.eq(): Eq<Boolean> =
  object : BooleanEq {}

fun Boolean.Companion.hash(): Hash<Boolean> =
  object : BooleanHash {}
