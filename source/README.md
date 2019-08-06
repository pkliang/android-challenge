# Tech Stack

* [Kotlin][1]
* [Kotlin Coroutines][2]
* [Koin][3]
* [Apollo GraphQL][4]
* [OkHttp][5]
* [Junit][6]
* [Mockk][7]
* [Hamcrest][8]
* [MvRx][9]


# Architecture: Clean + MvRx

There are three layers,

* `domain` contains business objects, usecases and repositories.

* `data` provides repository implementations. Apollo is used for Graphql requests.

* `ui` is the presentation layer which uses MvRx pattern. Conceptually, MvRx will feel very familiar for those who are used to React. Koin is used as DI in this layer

<img width="1381" alt="captura de pantalla 2018-02-23 a la s 11 39 01" src="https://user-images.githubusercontent.com/5893477/36608070-4cd45166-188e-11e8-977a-fc6e1cd8b359.png">


# Testing

JUnit and Mockk for unit tests are used here.

There is no UI tests(Espresso) due to time limit.


# Code Standard

[Detekt][10] is used for static code analysis for Kotlin.

.idea/codeSytles folder is shared in the repository.


# Optional extras Implemented
* Show avatar in the custom commit view
* Load more commits when reaching the end of the list

# Important
Replace `auth_token` with your own github access token with `repo` and `user` scopes in `strings.xml` when you run the app.

Otherwise you will end up with `401 Unauthorized`

[1]: https://kotlinlang.org/
[2]: https://github.com/Kotlin/kotlinx.coroutines
[3]: https://github.com/InsertKoinIO/koin
[4]: https://github.com/apollographql/apollo-android
[5]: https://github.com/square/okhttp
[6]: http://developer.android.com/intl/es/reference/junit/framework/package-summary.html
[7]: https://mockk.io/
[8]: http://hamcrest.org/
[9]: https://github.com/airbnb/MvRx
[10]: https://github.com/arturbosch/detekt
