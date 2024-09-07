# Kotlin Coroutines And Flows

## Coroutines Basics

Las coroutines (corrutinas) son una característica poderosa para manejar la concurrencia de manera más eficiente y sencilla. Son una forma de realizar operaciones asíncronas sin bloqueqr el hilo principal, permitiendo que tu aplicación sea más receptiva y eficiente en el uso de recursos.
- Más ligeras que los hilos
- Fácil de trabajar
- Fácil de cambiar entre hilos

## Flow

Flow es una API para emitir múltiples valores de manera asíncrona. Es una secuencia de valores que pueden ser emitidos de manera asíncrona, y que pueden ser consumidos de manera asíncrona. Es una forma de trabajar con secuencias de datos asíncronas de manera más eficiente y sencilla.
- Secuencia de valores
- Asíncrono
- Consumir de manera asíncrona

## Concurrencia

Es la capacidad de un sistema para gestionar múltiples tareas a la vez, sin  necesidad de que se ejecuten simultábenamente. Las tareas pueden empezar, detenerse, reanudarse y terminar en momentos distintos y el sustema es quien gestiona su orden y cómo terminarla.

## Paralelismo

Es la capacidad de un sistema para ejecutar múltiples tareas simultáneamente. Las tareas se ejecutan al mismo tiempo aprovechando múltiples núcleos de CPU a múltiples máquinas.


| Concurrencia                                                                                                                         | Paralelismo                                                                   |
| ------------------------------------------------------------------------------------------------------------------------------------ | ----------------------------------------------------------------------------- |
| Se trata de gestionar múltiples tareas que puedan progresar de forma alternada, pero no necesariamente ejecutándose al mismo tiempo. | Ejecuta múltiples tareas al mismo tiempo en diferentes hilos a núcleos de CPU |

## Suspend functions

Es una función especial que puede suspender su ejecución sin bloquear el hilo en el que se ejecuta, y luego reanudarse más tarde  cuando la operación suspendida esté completada.

Al usar funciones suspend, el código asíncrono parece sincrónico, lo que facilita si lectura y mantenimiento en comparación con otras técnicas de manejo de concurrencia como callbacks o promesas.


## Coroutines Scopes

Un coroutine scope (O "alcance de coroutine") es un entorno que controla la ejecución de coroutines, gestionando el ciclo de vida de estas y facilitando la estructura jerárquica de las coroutines, que define:

1. **El contexto**
   Incluye información como el Dispatcher (dónde se ejecuta la coroutine, ya sea en el hilo principal, I/O o un hilo de trabajo), y el Job que representa la tarea en ejecución.
2. **La vida útil**
   Permite controlar la cancelación de las coroutines que dependen de un scope determinado. Por ejemplo, si el scope se cancela, todas las coroutines que dependan de él también se cancelan automáticamente.

> [!NOTE]
> Los coroutines scopes garantizan que las corrutinas se cancelen adecuadamente cuando ya no son necesarias, evitando fugas de memoria o tareas innecesarias en segundo plano.


## Principales tipos de Coroutines Scopes

| Tipos                            | Descripción                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
| -------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **GlobalScope**                  | Es un scope global que lanza corrutinas que viven hasta que se cierra la aplicación                                                                                                                                                                                                                                                                                                                                                                                                            |
| **lifecycleScope** (Android)     | Está vinculado al ciclo de vida de un Activity o Fragment. Cuando el ciclo de vida se destruye, todas las corrutinas en este scope se cancelan automáticamente.                                                                                                                                                                                                                                                                                                                                |
| **viewModelScope** (Android)     | Similar a lifecycleScope, pero está ligado al ciclo de vida de un viewModel. Las coroutines se cancelan cuando el ViewModel es destruido.                                                                                                                                                                                                                                                                                                                                                      |
| **CoroutineScope** personalizado | Puedes crear scopes personalizados proporcionando un Job y un Dispatcher. Este scope es más flexible y útil para operaciones limitadas en tiempo y contexto.                                                                                                                                                                                                                          |
|                                  |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                |
| **runBlocking**                  | Bloquea el hilo actual mientras se ejecuta la coroutine. Es comúnmente usado en tests o para ejecutar coroutines en funciones `main()` que no son suspendidas.                                                                                                                                                                                                                                                                                                                                 |
| **MainScope**                    | Es un coroutine scope predefinido en Kotlin que se utiliza principalmente para lanzar coroutines que están asociadas con el hilo principal (generalmente usado para interactuar con la interfaz de usuario en aplicaciones Android o de escritorio)<br><br>Sin embargo, requiere que gestiones manualmente la cancelación de las coroutines para evitar problemas de memoria, lo cual se logra con la llamada a `scope.cancel()` cuando el ciclo de vida de la actividad o componente termina. |

Ejemplo de un **_CoroutineScope_**

```kotlin
val scope = CoroutineScope(Dispatcher.Main + SupervisorJob())
scope.cancel() 
```

## Uso común de CoroutineScope

1. **Cancelar coroutines**
   Uno de los principales beneficios de los scopes, es la capacidad de cancelar coroutines en grupo. Por ejemplo, cuando un Activity o ViewModel se destruye, todas las coroutines asociadas a su lifecycleScope se cancelan automáticamente, evitando fugas de memoria o tareas innecesarias.
   
3. **Dispatchers**
   Los dispatchers controlan en qué hilos se ejecutan los coroutines dentro de un scope. Algunos de ellos son:
   
   - **`Dispatchers.Main`**
     Corre en el hilo principal, comúnmente usado para actualizar la UI en aplicaciones Android.
   - **`Dispatchers.IO`**
     Usado para operaciones de entrada/salida como llamadas a la red o lecturas de archivos.
   - **`Dispatchers.Default`**
     Ideal para operaciones que requieren de uso intensivo de la CPU.
     
3. **Scopes personalizados**
   A veces, necesitas un control más granular sobre el ciclo de vida de una coroutine. En estos casos, puede crear un scope personalizado con un Job y asignar coroutines a este.


## Jobs & Deferred

### Job
Un `Job` en Kotlin representa una unidad de trabajo que se puede ejecutar en una coroutine. Un Job puede estar en diferentes estados como: `activo`, `completado` o `cancelado`, y se usa principalmente para mejorar el ciclo de vida de una coroutine.

- `job.isActive`
- `job.isCompleted`
- `job.isCancelled`

> [!NOTE]
> El método `.join()` se usa para esperar a que el Job termine


### Async
Async es un función que lanza una coroutine de manera similar a launch pero a diferencia de launch, async está diseñado para devolver un valor mediante un objeto `Deferred`.

Mientras que launch se usa para coroutines que no retornar un valor, async se utiliza cuando necesitas obtener un resultado de forma asíncrona.

```kotlin
val deffered = async {
	delay(1000L)
	"Resultado de async"
}

val result = deferred.await() // Espera y obtiene el resultado
```

### Await
await es una función que se usa junto con async para esperar el resultado de una coroutine lanzada con async. La llamada await es suspendida, lo que significa que no bloquea el hilo mientras espera que la operación asíncrona termine.


### Deferred
Deferred es una interfaz que representa un futuro resultado que se obtendrá más adelante. Es el tipo de retorno de una coroutine que ha sido lanzada con async.

Puedes pensar en Deferred como una promesa de que se devolverá un valor en un futuro y que puede usar `await()` para obtener ese valor cuando esté disponible.

Deferred extiende Job, lo que significa que también se puede usar para manejar el ciclo de vida de la coroutine (cancelarla, esperar su finalización, etc) pero la con la diferencia de que devuelve un valor.


| Concepto     | Función principal                                                          | Resultado devuelto           | Uso común                                       |
| ------------ | -------------------------------------------------------------------------- | ---------------------------- | ----------------------------------------------- |
| **Job**      | Representa una tarea o trabajo en ejecución dentro de una coroutine.       | Unit<br>No devuelve un valor | Para ejecutar tareas que no devuelven un valor. |
| **async**    | Lanza una coroutine que devuelve un valor futuro (deferred)                | `Deferred<T>`                | Para tareas que devuelven un valor.             |
| **await**    | Suspende la ejecución de la coroutine hasta obtener el resultado de async. | El valor resultante de async | Para obtener el valor retornado por async.      |
| **Deferred** | Representa un valor futuro que será calculado de forma asincrónica.        | El valor que retorna async   | Para manejar resultados asincrónicos.           |

## Contenido

- [Coroutine Context](docs/CoroutineContext.md)
- [Coroutine Cancellation](docs/CoroutineCancellation.md)
- [Coroutine Error Handling](docs/CoroutineErrorHandling.md)
- [Coroutine Synchronization](docs/CoroutineSynchronization.md)
- [Flows Fundamentals](docs/FlowsFundamentals.md)
- [Testing Coroutines & Flows](docs/TestingCoroutinesAndFlows.md)

### Assignments

- [Assignment 1](app/src/main/java/com/crexative/kotlincoroutinesandflows/assigments/Assignment1.kt)