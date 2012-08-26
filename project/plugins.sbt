
resolvers += "gseitz@github" at "http://gseitz.github.com/maven/"

resolvers += Resolver.url("sbt-plugin-releases",
                          new URL("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases/")
)(Resolver.ivyStylePatterns)

addSbtPlugin("com.github.gseitz" % "sbt-protobuf" % "0.2.2")

addSbtPlugin("com.github.retronym" % "sbt-onejar" % "0.8")
