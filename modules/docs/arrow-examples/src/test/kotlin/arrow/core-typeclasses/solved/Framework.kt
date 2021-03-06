@file:Suppress("unused")

package com.pacoworks.typeclasses.basics.solved

import arrow.Kind
import arrow.core.Try
import arrow.core.left
import arrow.core.right
import arrow.effects.typeclasses.Async
import arrow.core.typeclasses.Applicative
import arrow.core.typeclasses.ApplicativeError
import arrow.core.typeclasses.DaoDatabase
import arrow.core.typeclasses.Index
import arrow.core.typeclasses.NetworkModule
import arrow.core.typeclasses.UserDao
import arrow.core.typeclasses.UserDto

// Step 0 - extract interface

interface NetworkOperations {
  val network: NetworkModule

  fun Index.requestUser(): Try<UserDto> =
    Try { network.fetch(this, mapOf("1" to "2")) }
}

interface DaoOperations {
  val dao: DaoDatabase

  fun Index.queryUser(): Try<UserDao> =
    Try { dao.query("SELECT * from Users where userId = $this") }

  fun Index.queryCompany(): Try<UserDao> =
    Try { dao.query("SELECT * from Companies where companyId = $this") }
}

// We should probably abstract the Mapper too

// Step 1 - wrap

interface NetworkOperationsUnsafe<F> : Applicative<F> {
  val network: NetworkModule

  fun Index.requestUser(): Kind<F, UserDto> =
    just(network.fetch(this, mapOf("1" to "2")))
}

interface DaoOperationsUnsafe<F> : Applicative<F> {
  val dao: DaoDatabase

  fun Index.queryUser(): Kind<F, UserDao> =
    just(dao.query("SELECT * from Users where userId = $this"))
}

// Okay, but those calls can throw exceptions 😱

interface NetworkOperationsSync<F> : ApplicativeError<F, Throwable> {
  val network: NetworkModule

  fun Index.requestUser(): Kind<F, UserDto> =
    try {
      just(network.fetch(this, mapOf("1" to "2")))
    } catch (t: Throwable) {
      raiseError(t)
    }
  // catch { network.fetch(this, mapOf("1" to "2")) }
}

interface DaoOperationsSync<F> : ApplicativeError<F, Throwable> {
  val dao: DaoDatabase

  fun Index.queryUser(): Kind<F, UserDao> =
    catch { dao.query("SELECT * from Users where userId = $this") }
}

// Let's see how these changes can also be applied to the Mapper

// Step 2 - Make operations asynchronous

interface NetworkOperationsAsync<F> : Async<F> {
  val network: NetworkModule

  fun Index.requestUser(): Kind<F, UserDto> =
    async { callback ->
      network.fetchAsync(this, mapOf("1" to "2"),
        { err -> callback(err.left()) },
        { value -> callback(value.right()) })
    }
}

interface DaoOperationsAsync<F> : Async<F> {
  val dao: DaoDatabase

  fun Index.queryUser(): Kind<F, UserDao> =
    async { callback ->
      dao.queryAsync("SELECT * from Users where userId = $this",
        { err -> callback(err.left()) },
        { value -> callback(value.right()) })
    }
}

// Awesome, let's go back to our Business Logic
