package com.learnathome.api

import java.io.IOException

class ApiExceptions (message : String) : IOException(message)
class NoInternetException(message: String) : IOException(message)