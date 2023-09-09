package com.abt.tests.fixtures

trait DeviceFixtures {
  val deviceList: String =
    """[
      |{"_id":{"$oid":"5b0586cbbb95a07d93169663"},"name":"device_one","polled":{"$date":"2023-02-01T15:28:35.321Z"}},
      |{"_id":{"$oid":"5b0586dabb95a07d93169665"},"name":"device_two","polled":{"$date":"2023-02-01T16:28:35.321Z"}},
      |{"_id":{"$oid":"5beb230295259025f65cb19a"},"name":"device_three","polled":{"$date":"2023-02-01T17:28:35.321Z"}},
      |{"_id":{"$oid":"5beb230395259025f65cb19c"},"name":"device_four","polled":{"$date":"2023-02-01T18:28:35.321Z"}},
      |{"_id":{"$oid":"5beb230395259025f65cb19e"},"name":"device_five","polled":{"$date":"2023-02-01T19:28:35.321Z"}},
      |{"_id":{"$oid":"5beb230395259025f65cb1a0"},"name":"device_six","polled":{"$date":"2023-02-01T20:28:35.321Z"}},
      |{"_id":{"$oid":"5beb230395259025f65cb1a2"},"name":"device_seven","polled":{"$date":"2023-02-01T21:28:35.321Z"}},
      |{"_id":{"$oid":"5c8800da41b73e0b38991f2f"},"name":"device_eight","polled":{"$date":"2023-02-01T22:28:35.321Z"}},
      |{"_id":{"$oid":"5c8800db41b73e0b38991f31"},"name":"device_none","polled":{"$date":"2023-02-01T23:28:35.321Z"}}]""".stripMargin.linesIterator
      .mkString("")
      .trim
}
