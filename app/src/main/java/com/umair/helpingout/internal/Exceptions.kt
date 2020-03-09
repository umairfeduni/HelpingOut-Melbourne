
package com.umair.helpingout.internal


import java.io.IOException

class NoConnectivityException: IOException()
class ServerDownException: IOException()

class LocationPermissionNotGrantedException: Exception()
class DateNotFoundException: Exception()