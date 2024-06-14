package nl.xanderdekeijzer.rides.active

import nl.xanderdekeijzer.rides.data.CartData

class Train(
    val id: String,
    val track: Any,
    val carts: ArrayList<CartData> = arrayListOf()
) {
    constructor(id: Int, track: Any, carts: ArrayList<CartData> = arrayListOf()): this(id.toString(), track, carts)
}