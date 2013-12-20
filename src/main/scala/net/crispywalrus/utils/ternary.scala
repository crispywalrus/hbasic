package net.crispywalrus.utils {

  import language.implicitConversions

  object Ternary {
    implicit def coalescingOperator[T](pred: T) = new {
      def ??[A >: T](alt: =>A) = if(pred == null) alt else pred
    }

    implicit def elvisOperator[T](alt: =>T) = new {
      def ?:[A >: T](pred: A) = if(pred == null) alt else pred
    }
  }

}
