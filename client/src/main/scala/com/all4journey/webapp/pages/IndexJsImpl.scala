package com.all4journey.webapp.pages

import scala.scalajs.js.Dynamic.global

// $COVERAGE-OFF$
/**
  * Created by rjkj on 12/5/15.
  */
object IndexJsImpl extends IndexJs{
  def run(): Unit = {
    global.document.getElementById("content").textContent = "More content to come!"
  }
}

// $COVERAGE-ON$


