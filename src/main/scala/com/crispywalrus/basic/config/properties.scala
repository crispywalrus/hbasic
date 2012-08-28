package com.crispywalrus.basic.config

import java.io.{ IOException, PrintWriter }

/**
 * totally stolen from scala.util.Properties due to the fact that
 * PropertiesTrait is private to the scala package.
 *
 */
trait PropertiesTrait {
  protected def propCategory: String      // specializes the remainder of the values
  protected def pickJarBasedOn: Class[_]  // props file comes from jar containing this
  
  /** The name of the properties file */
  protected val propFilename = "/" + propCategory + ".properties"
  
  /** The loaded properties */
  lazy val props: java.util.Properties = {
    val props = new java.util.Properties
    val stream = pickJarBasedOn getResourceAsStream propFilename
    if (stream ne null)
      quietlyDispose(props load stream, stream.close)
    
    props
  }   
  
  private def quietlyDispose(action: => Unit, disposal: => Unit) =
    try     { action }
  finally { 
    try     { disposal }
    catch   { case _: IOException => }
  }
  
  def propIsSet(name: String)                   = System.getProperty(name) != null
  def propIsSetTo(name: String, value: String)  = propOrNull(name) == value
  def propOrElse(name: String, alt: String)     = System.getProperty(name, alt)
  def propOrEmpty(name: String)                 = propOrElse(name, "")
  def propOrNull(name: String)                  = propOrElse(name, null)
  def propOrNone(name: String)                  = Option(propOrNull(name))
  def propOrFalse(name: String)                 = propOrNone(name) exists (x => List("yes", "on", "true") contains x.toLowerCase)
  def setProp(name: String, value: String)      = System.setProperty(name, value)
  def clearProp(name: String)                   = System.clearProperty(name)
  
  def envOrElse(name: String, alt: String)      = Option(System getenv name) getOrElse alt
  def envOrNone(name: String)                   = Option(System getenv name)
  
  /** Various well-known properties.
  */
  def javaClassPath         = propOrEmpty("java.class.path")
  def javaHome              = propOrEmpty("java.home")
  def javaVendor            = propOrEmpty("java.vendor")
  def javaVersion           = propOrEmpty("java.version")
  def javaVmInfo            = propOrEmpty("java.vm.info")
  def javaVmName            = propOrEmpty("java.vm.name")
  def javaVmVendor          = propOrEmpty("java.vm.vendor")
  def javaVmVersion         = propOrEmpty("java.vm.version")
  def osName                = propOrEmpty("os.name")
  def scalaHome             = propOrEmpty("scala.home")
  def tmpDir                = propOrEmpty("java.io.tmpdir")
  def userDir               = propOrEmpty("user.dir")
  def userHome              = propOrEmpty("user.home")
  def userName              = propOrEmpty("user.name")
  
  /** Can the java version be determined to be at least as high as the argument?
   *  Hard to properly future proof this but at the rate 1.7 is going we can leave
   *  the issue for our cyborg grandchildren to solve.
   */
  def isJavaAtLeast(version: String) = {
    val okVersions = version match {
      case "1.5"    => List("1.5", "1.6", "1.7")
      case "1.6"    => List("1.6", "1.7")
      case "1.7"    => List("1.7")
      case _        => Nil
    }
    okVersions exists (javaVersion startsWith _)
  }

  // for values based on propFilename
  def getOrElse(name: String, alt: String): String = props.getProperty(name, alt)
  def getOrEmpty(name: String): String             = getOrElse(name, "")
  def getOrNone(name: String): Option[String]      = Option(props.getProperty(name))
}
