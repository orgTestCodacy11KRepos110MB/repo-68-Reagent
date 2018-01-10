/*
 * Copyright 2017 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:JvmName("Tasks")
package reagent

import java.util.concurrent.Callable

@JvmName("fromRunnable")
fun Runnable.asTask(): Task = TaskFromRunnable(this)

internal class TaskFromRunnable(private val func: Runnable) : Task() {
  override suspend fun subscribe(observer: Observer) {
    try {
      func.run()
    } catch (t: Throwable) {
      observer.onError(t)
      return
    }
    observer.onComplete()
  }
}

@JvmName("fromCallable")
fun Callable<*>.asTask(): Task = TaskFromCallable(this)

internal class TaskFromCallable(private val func: Callable<*>) : Task() {
  override suspend fun subscribe(observer: Observer) {
    try {
      func.call()
    } catch (t: Throwable) {
      observer.onError(t)
      return
    }
    observer.onComplete()
  }
}