package com.sample.vkoelassign.utility

import java.io.IOException


/**
 *
 * Purpose – Exceptions list
 *
 * @author Rishabh Gupta
 *
 * */
class ApiException(message: String) : IOException(message)
class NoInternetException(message: String) : IOException(message)