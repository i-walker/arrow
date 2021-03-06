package arrow.mtl.extensions

import arrow.Kind
import arrow.core.Option
import arrow.mtl.typeclasses.FunctorFilter
import arrow.mtl.typeclasses.TraverseFilter
import arrow.core.typeclasses.Applicative
import arrow.core.typeclasses.ComposedFunctor
import arrow.core.typeclasses.ComposedTraverse
import arrow.core.typeclasses.Functor
import arrow.core.typeclasses.Nested
import arrow.core.typeclasses.Traverse
import arrow.core.typeclasses.nest
import arrow.core.typeclasses.unnest

interface ComposedFunctorFilter<F, G> : FunctorFilter<Nested<F, G>>, ComposedFunctor<F, G> {

  override fun F(): Functor<F>

  override fun G(): FunctorFilter<G>

  override fun <A, B> Kind<Nested<F, G>, A>.mapFilter(f: (A) -> Option<B>): Kind<Nested<F, G>, B> =
    F().run { unnest().map { G().run { it.mapFilter(f) } }.nest() }

  fun <A, B> mapFilterC(fga: Kind<F, Kind<G, A>>, f: (A) -> Option<B>): Kind<F, Kind<G, B>> =
    fga.nest().mapFilter(f).unnest()

  companion object {
    operator fun <F, G> invoke(FF: Functor<F>, FFG: FunctorFilter<G>): ComposedFunctorFilter<F, G> =
      object : ComposedFunctorFilter<F, G> {
        override fun F(): Functor<F> = FF

        override fun G(): FunctorFilter<G> = FFG
      }
  }
}

interface ComposedTraverseFilter<F, G> :
  TraverseFilter<Nested<F, G>>,
  ComposedTraverse<F, G> {

  override fun FT(): Traverse<F>

  override fun GT(): TraverseFilter<G>

  override fun GA(): Applicative<G>

  override fun <H, A, B> Kind<Nested<F, G>, A>.traverseFilter(AP: Applicative<H>, f: (A) -> Kind<H, Option<B>>): Kind<H, Kind<Nested<F, G>, B>> = AP.run {
    FT().run { unnest().traverse(AP) { ga -> GT().run { ga.traverseFilter(AP, f) } } }.map { it.nest() }
  }

  fun <H, A, B> traverseFilterC(fa: Kind<F, Kind<G, A>>, f: (A) -> Kind<H, Option<B>>, HA: Applicative<H>): Kind<H, Kind<Nested<F, G>, B>> =
    fa.nest().traverseFilter(HA, f)

  companion object {
    operator fun <F, G> invoke(
      FF: Traverse<F>,
      GF: TraverseFilter<G>,
      GA: Applicative<G>
    ): ComposedTraverseFilter<F, G> =
      object : ComposedTraverseFilter<F, G> {
        override fun FT(): Traverse<F> = FF

        override fun GT(): TraverseFilter<G> = GF

        override fun GA(): Applicative<G> = GA
      }
  }
}
