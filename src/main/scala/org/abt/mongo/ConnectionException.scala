package org.abt.mongo

trait ConnectionException extends Exception

object ConnectionException {
    def apply(message: String) = new Exception(message) with ConnectionException
    def apply(cause: Throwable) = new Exception(cause) with ConnectionException
    def apply(message: String, cause: Throwable) = new Exception(message, cause) with ConnectionException
}