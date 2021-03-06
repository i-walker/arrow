@file:Suppress("unused")

package arrow.dagger.extensions.tests

import arrow.core.EitherPartialOf
import arrow.core.ForEval
import arrow.core.ForFunction0
import arrow.core.ForId
import arrow.core.ForOption
import arrow.core.ForTry
import arrow.core.Function1PartialOf
import arrow.dagger.extensions.ArrowInstances
import arrow.dagger.extensions.CoproductInstances
import arrow.dagger.extensions.EitherInstances
import arrow.dagger.extensions.EitherTInstances
import arrow.dagger.extensions.Function1Instances
import arrow.dagger.extensions.KleisliInstances
import arrow.dagger.extensions.MapKInstances
import arrow.dagger.extensions.OptionTInstances
import arrow.dagger.extensions.SortedMapKInstances
import arrow.dagger.extensions.StateTInstances
import arrow.data.CoproductPartialOf
import arrow.data.EitherTPartialOf
import arrow.data.ForListK
import arrow.data.ForNonEmptyList
import arrow.data.ForSequenceK
import arrow.data.ForSetK
import arrow.data.KleisliPartialOf
import arrow.data.MapKPartialOf
import arrow.data.OptionTPartialOf
import arrow.data.SortedMapKPartialOf
import arrow.data.StateTPartialOf
import arrow.core.typeclasses.Applicative
import arrow.core.typeclasses.ApplicativeError
import arrow.core.typeclasses.Bimonad
import arrow.core.typeclasses.Comonad
import arrow.core.typeclasses.Eq
import arrow.core.typeclasses.Foldable
import arrow.core.typeclasses.Functor
import arrow.core.typeclasses.Hash
import arrow.core.typeclasses.Monad
import arrow.core.typeclasses.MonadError
import arrow.core.typeclasses.Monoid
import arrow.core.typeclasses.MonoidK
import arrow.core.typeclasses.Order
import arrow.core.typeclasses.Semigroup
import arrow.core.typeclasses.SemigroupK
import arrow.core.typeclasses.Traverse
import dagger.Component
import dagger.Module
import javax.inject.Singleton

@Module
class LocalCoproductInstances : CoproductInstances<ForOption, ForListK>()

@Module
class LocalEitherInstances : EitherInstances<Unit>()

@Module
class LocalEitherTInstances : EitherTInstances<ForOption, Unit>()

@Module
class LocalOptionTTInstances : OptionTInstances<ForOption>()

@Module
class LocalFunction1Instances : Function1Instances<ForOption>()

@Module
class LocalKleisliInstances : KleisliInstances<ForOption, Unit>()

@Module
class LocalMapKInstances : MapKInstances<String>()

@Module
class LocalSortedMapKInstances : SortedMapKInstances<String>()

@Module
class LocalStateTInstances : StateTInstances<ForOption, Unit>()

typealias F = ForOption
typealias G = ForListK
typealias L = Unit
typealias D = Unit
typealias S = Unit
typealias K = String

/**
 * If the component below compiles it means the `ArrowInstances` was successful resolving
 * all the declared below instances at compile time along with the helper for instances that take type params
 * and need explicit evidence of a @Module such as `LocalStateTInstances`
 */
@Singleton
@Component(modules = [
  ArrowInstances::class,
  LocalCoproductInstances::class,
  LocalEitherInstances::class,
  LocalEitherTInstances::class,
  LocalFunction1Instances::class,
  LocalKleisliInstances::class,
  LocalMapKInstances::class,
  LocalOptionTTInstances::class,
  LocalSortedMapKInstances::class,
  LocalStateTInstances::class
])
interface Runtime {
  fun coproductFunctor(): Functor<CoproductPartialOf<F, G>>
  fun coproductComonad(): Functor<CoproductPartialOf<F, G>>
  fun coproductFoldable(): Foldable<CoproductPartialOf<F, G>>
  fun coproductTraverse(): Traverse<CoproductPartialOf<F, G>>
  fun eitherFunctor(): Functor<EitherPartialOf<L>>
  fun eitherApplicative(): Applicative<EitherPartialOf<L>>
  fun eitherMonad(): Monad<EitherPartialOf<L>>
  fun eitherFoldable(): Foldable<EitherPartialOf<L>>
  fun eitherTraverse(): Traverse<EitherPartialOf<L>>
  fun eitherSemigroupK(): SemigroupK<EitherPartialOf<L>>
  fun eitherTFunctor(): Functor<EitherTPartialOf<F, L>>
  fun eitherTApplicative(): Applicative<EitherTPartialOf<F, L>>
  fun eitherTMonad(): Monad<EitherTPartialOf<F, L>>
  fun eitherTApplicativeError(): ApplicativeError<EitherTPartialOf<F, L>, L>
  fun eitherTMonadError(): MonadError<EitherTPartialOf<F, L>, L>
  fun eitherTFoldable(): Foldable<EitherTPartialOf<F, L>>
  fun eitherTTraverse(): Traverse<EitherTPartialOf<F, L>>
  fun eitherTSemigroupK(): SemigroupK<EitherTPartialOf<F, L>>
  fun evalFunctor(): Functor<ForEval>
  fun evalApplicative(): Applicative<ForEval>
  fun evalMonad(): Monad<ForEval>
  fun evalComonad(): Comonad<ForEval>
  fun evalBimonad(): Bimonad<ForEval>
  fun function0Functor(): Functor<ForFunction0>
  fun function0Applicative(): Applicative<ForFunction0>
  fun function0Monad(): Monad<ForFunction0>
  fun function0Comonad(): Comonad<ForFunction0>
  fun function0Bimonad(): Bimonad<ForFunction0>
  fun function1Functor(): Functor<Function1PartialOf<F>>
  fun function1Applicative(): Applicative<Function1PartialOf<F>>
  fun function1Monad(): Monad<Function1PartialOf<F>>
  fun idFunctor(): Functor<ForId>
  fun idApplicative(): Applicative<ForId>
  fun idMonad(): Monad<ForId>
  fun idComonad(): Comonad<ForId>
  fun idBimonad(): Bimonad<ForId>
  fun kleisliFunctor(): Functor<KleisliPartialOf<F, D>>
  fun kleisliApplicative(): Applicative<KleisliPartialOf<F, D>>
  fun kleisliMonad(): Monad<KleisliPartialOf<F, D>>
  fun kleisliApplicativeError(): ApplicativeError<KleisliPartialOf<F, D>, D>
  fun kleisliMonadError(): MonadError<KleisliPartialOf<F, D>, D>
  fun listKFunctor(): Functor<ForListK>
  fun listKApplicative(): Applicative<ForListK>
  fun listKMonad(): Monad<ForListK>
  fun listKFoldable(): Foldable<ForListK>
  fun listKTraverse(): Traverse<ForListK>
  fun listKSemigroupK(): SemigroupK<ForListK>
  fun listKMonoidK(): MonoidK<ForListK>
  fun mapKFunctor(): Functor<MapKPartialOf<K>>
  fun mapKFoldable(): Foldable<MapKPartialOf<K>>
  fun mapKTraverse(): Traverse<MapKPartialOf<K>>
  fun nonEmptyListFunctor(): Functor<ForNonEmptyList>
  fun nonEmptyListApplicative(): Applicative<ForNonEmptyList>
  fun nonEmptyListMonad(): Monad<ForNonEmptyList>
  fun nonEmptyListFoldable(): Foldable<ForNonEmptyList>
  fun nonEmptyListTraverse(): Traverse<ForNonEmptyList>
  fun nonEmptyListSemigroupK(): SemigroupK<ForNonEmptyList>
  fun nonEmptyListComonad(): Comonad<ForNonEmptyList>
  fun nonEmptyListBimonad(): Bimonad<ForNonEmptyList>
  fun byteSemigroup(): Semigroup<Byte>
  fun byteMonoid(): Monoid<Byte>
  fun byteOrder(): Order<Byte>
  fun byteEq(): Eq<@JvmSuppressWildcards Byte>
  fun byteHash(): Hash<Byte>
  fun doubleSemigroup(): Semigroup<Double>
  fun doubleMonoid(): Monoid<Double>
  fun doubleOrder(): Order<Double>
  fun doubleEq(): Eq<@JvmSuppressWildcards Double>
  fun doubleHash(): Hash<Double>
  fun intSemigroup(): Semigroup<Int>
  fun intMonoid(): Monoid<Int>
  fun intOrder(): Order<Int>
  fun intEq(): Eq<@JvmSuppressWildcards Int>
  fun intHash(): Hash<Int>
  fun longSemigroup(): Semigroup<Long>
  fun longMonoid(): Monoid<Long>
  fun longOrder(): Order<Long>
  fun longEq(): Eq<@JvmSuppressWildcards Long>
  fun longHash(): Hash<Long>
  fun shortSemigroup(): Semigroup<Short>
  fun shortMonoid(): Monoid<Short>
  fun shortOrder(): Order<Short>
  fun shortEq(): Eq<@JvmSuppressWildcards Short>
  fun shortHash(): Hash<Short>
  fun floatSemigroup(): Semigroup<Float>
  fun floatMonoid(): Monoid<Float>
  fun floatOrder(): Order<Float>
  fun floatEq(): Eq<@JvmSuppressWildcards Float>
  fun floatHash(): Hash<Float>
  fun optionFunctor(): Functor<ForOption>
  fun optionApplicative(): Applicative<ForOption>
  fun optionMonad(): Monad<ForOption>
  fun optionMonadError(): MonadError<ForOption, Unit>
  fun optionFoldable(): Foldable<ForOption>
  fun optionTraverse(): Traverse<ForOption>
  fun optionTFunctor(): Functor<OptionTPartialOf<F>>
  fun optionTApplicative(): Applicative<OptionTPartialOf<F>>
  fun optionTMonad(): Monad<OptionTPartialOf<F>>
  fun optionTFoldable(): Foldable<OptionTPartialOf<F>>
  fun optionTTraverse(): Traverse<OptionTPartialOf<F>>
  fun optionTSemigroupK(): SemigroupK<OptionTPartialOf<F>>
  fun optionTMonoidK(): MonoidK<OptionTPartialOf<F>>
  fun sequenceKFunctor(): Functor<ForSequenceK>
  fun sequenceKApplicative(): Applicative<ForSequenceK>
  fun sequenceKMonad(): Monad<ForSequenceK>
  fun sequenceKFoldable(): Foldable<ForSequenceK>
  fun sequenceKTraverse(): Traverse<ForSequenceK>
  fun sequenceKMonoidK(): MonoidK<ForSequenceK>
  fun sequenceKSemigroupK(): SemigroupK<ForSequenceK>
  fun setKFoldable(): Foldable<ForSetK>
  fun setKMonoidK(): MonoidK<ForSetK>
  fun setKSemigroupK(): SemigroupK<ForSetK>
  fun sortedMapKFunctor(): Functor<SortedMapKPartialOf<K>>
  fun sortedMapKFoldable(): Foldable<SortedMapKPartialOf<K>>
  fun sortedMapKTraverse(): Traverse<SortedMapKPartialOf<K>>
  fun stateTFunctor(): Functor<StateTPartialOf<F, S>>
  fun stateTApplicative(): Applicative<StateTPartialOf<F, S>>
  fun stateTMonad(): Monad<StateTPartialOf<F, S>>
  fun stateTApplicativeError(): ApplicativeError<StateTPartialOf<F, S>, S>
  fun stateTMonadError(): MonadError<StateTPartialOf<F, S>, S>
  fun stringSemigroup(): Semigroup<String>
  fun stringMonoid(): Monoid<String>
  fun stringEq(): Eq<@JvmSuppressWildcards String>
  fun tryFunctor(): Functor<ForTry>
  fun tryApplicative(): Applicative<ForTry>
  fun tryMonad(): Monad<ForTry>
  fun tryMonadError(): MonadError<ForTry, Throwable>
  fun tryFoldable(): Foldable<ForTry>
  fun tryTraverse(): Traverse<ForTry>
}

object Arrow {
  val instances = DaggerRuntime.builder().build()
}

object test {
  @JvmStatic
  fun main() {
    println(Arrow.instances.optionApplicative().just(1))
  }
}
