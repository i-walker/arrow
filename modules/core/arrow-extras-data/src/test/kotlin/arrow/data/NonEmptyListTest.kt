package arrow.data

import arrow.core.extensions.eq
import arrow.core.extensions.hash
import arrow.data.extensions.nonemptylist.applicative.applicative
import arrow.data.extensions.nonemptylist.comonad.comonad
import arrow.data.extensions.nonemptylist.eq.eq
import arrow.data.extensions.nonemptylist.hash.hash
import arrow.data.extensions.nonemptylist.monad.monad
import arrow.data.extensions.nonemptylist.semigroup.semigroup
import arrow.data.extensions.nonemptylist.semigroupK.semigroupK
import arrow.data.extensions.nonemptylist.show.show
import arrow.data.extensions.nonemptylist.traverse.traverse
import arrow.test.UnitSpec
import arrow.test.laws.ComonadLaws
import arrow.test.laws.HashLaws
import arrow.test.laws.MonadLaws
import arrow.test.laws.SemigroupKLaws
import arrow.test.laws.SemigroupLaws
import arrow.test.laws.ShowLaws
import arrow.test.laws.TraverseLaws
import arrow.core.typeclasses.Eq
import io.kotlintest.runner.junit4.KotlinTestRunner
import org.junit.runner.RunWith

@RunWith(KotlinTestRunner::class)
class NonEmptyListTest : UnitSpec() {
  init {

    val EQ = NonEmptyList.eq(Int.eq())
    testLaws(
      ShowLaws.laws(NonEmptyList.show(), EQ) { it.nel() },
      MonadLaws.laws(NonEmptyList.monad(), Eq.any()),
      SemigroupKLaws.laws(
        NonEmptyList.semigroupK(),
        NonEmptyList.applicative(),
        Eq.any()),
      ComonadLaws.laws(NonEmptyList.comonad(), { NonEmptyList.of(it) }, Eq.any()),
      TraverseLaws.laws(NonEmptyList.traverse(), NonEmptyList.applicative(), { n: Int -> NonEmptyList.of(n) }, Eq.any()),
      SemigroupLaws.laws(NonEmptyList.semigroup(), Nel(1, 2, 3), Nel(3, 4, 5), Nel(6, 7, 8), NonEmptyList.eq(Int.eq())),
      HashLaws.laws(NonEmptyList.hash(Int.hash()), EQ) { Nel.of(it) }
    )
  }
}
