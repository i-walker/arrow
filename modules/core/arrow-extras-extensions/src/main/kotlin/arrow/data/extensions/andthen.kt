package arrow.data.extensions

import arrow.Kind
import arrow.Kind2
import arrow.core.Either
import arrow.data.AndThen
import arrow.data.AndThenOf
import arrow.data.AndThenPartialOf
import arrow.data.ForAndThen
import arrow.data.fix
import arrow.data.invoke
import arrow.extension
import arrow.core.typeclasses.Applicative
import arrow.core.typeclasses.Apply
import arrow.core.typeclasses.Category
import arrow.core.typeclasses.Conested
import arrow.core.typeclasses.Contravariant
import arrow.core.typeclasses.Functor
import arrow.core.typeclasses.Monad
import arrow.core.typeclasses.Monoid
import arrow.core.typeclasses.Profunctor
import arrow.core.typeclasses.Semigroup
import arrow.core.typeclasses.conest
import arrow.core.typeclasses.counnest

@extension
interface AndThenSemigroup<A, B> : Semigroup<AndThen<A, B>> {
  fun SB(): Semigroup<B>

  override fun AndThen<A, B>.combine(b: AndThen<A, B>): AndThen<A, B> = SB().run {
    AndThen { a: A -> invoke(a).combine(b.invoke(a)) }
  }
}

@extension
interface AndThenMonoid<A, B> : Monoid<AndThen<A, B>>, AndThenSemigroup<A, B> {

  fun MB(): Monoid<B>

  override fun SB(): Semigroup<B> = MB()

  override fun empty(): AndThen<A, B> =
    AndThen { MB().empty() }
}

@extension
interface AndThenFunctor<X> : Functor<AndThenPartialOf<X>> {
  override fun <A, B> AndThenOf<X, A>.map(f: (A) -> B): AndThen<X, B> =
    fix().map(f)
}

@extension
interface AndThenApply<X> : Apply<AndThenPartialOf<X>>, AndThenFunctor<X> {
  override fun <A, B> AndThenOf<X, A>.ap(ff: AndThenOf<X, (A) -> B>): AndThen<X, B> =
    fix().ap(ff)

  override fun <A, B> AndThenOf<X, A>.map(f: (A) -> B): AndThen<X, B> =
    fix().map(f)
}

@extension
interface AndThenApplicative<X> : Applicative<AndThenPartialOf<X>>, AndThenFunctor<X> {
  override fun <A> just(a: A): AndThenOf<X, A> =
    AndThen.just(a)

  override fun <A, B> AndThenOf<X, A>.ap(ff: AndThenOf<X, (A) -> B>): AndThen<X, B> =
    fix().ap(ff)

  override fun <A, B> AndThenOf<X, A>.map(f: (A) -> B): AndThen<X, B> =
    fix().map(f)
}

@extension
interface AndThenMonad<X> : Monad<AndThenPartialOf<X>>, AndThenApplicative<X> {
  override fun <A, B> AndThenOf<X, A>.flatMap(f: (A) -> AndThenOf<X, B>): AndThen<X, B> =
    fix().flatMap(f)

  override fun <A, B> tailRecM(a: A, f: (A) -> AndThenOf<X, Either<A, B>>): AndThen<X, B> =
    AndThen.tailRecM(a, f)

  override fun <A, B> AndThenOf<X, A>.map(f: (A) -> B): AndThen<X, B> =
    fix().map(f)

  override fun <A, B> AndThenOf<X, A>.ap(ff: AndThenOf<X, (A) -> B>): AndThen<X, B> =
    fix().ap(ff)
}

@extension
interface AndThenCategory : Category<ForAndThen> {
  override fun <A> id(): AndThen<A, A> =
    AndThen.id()

  override fun <A, B, C> AndThenOf<B, C>.compose(arr: Kind2<ForAndThen, A, B>): AndThen<A, C> =
    fix().compose(arr::invoke)
}

@extension
interface AndThenContravariant<O> : Contravariant<Conested<ForAndThen, O>> {

  override fun <A, B> Kind<Conested<ForAndThen, O>, A>.contramap(f: (B) -> A): Kind<Conested<ForAndThen, O>, B> =
    counnest().fix().contramap(f).conest()
}

@extension
interface AndThenProfunctor : Profunctor<ForAndThen> {
  override fun <A, B, C, D> AndThenOf<A, B>.dimap(fl: (C) -> A, fr: (B) -> D): AndThen<C, D> =
    fix().andThen(fr).compose(fl)
}
