package auth

import java.net.URLEncoder

object UrlBuilder {

  def buildUrl(location: String, params: (String, String)*): String = {
    params match {
      case p if p.nonEmpty =>
        val paramString = params.foldLeft("") { (a, t) =>
          a + s"${t._1}=${URLEncoder.encode(t._2)}&"
        }.dropRight(1)

        s"$location?$paramString"

      case _ => location
    }
  }
}
