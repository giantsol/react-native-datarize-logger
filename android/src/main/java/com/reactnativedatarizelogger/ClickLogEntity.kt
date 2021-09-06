package com.reactnativedatarizelogger

internal data class ClickLogEntity(
  val meta: MetaEntity,
  val click: ClickEntity,
  val impression: List<ViewEntity>,
)
