package com.github.bhop.sparkml.common

object Resources {

  def resourcePath(resource: String): String = getClass.getResource("/" + resource).getPath
}
