package nl.xanderdekeijzer.rides.active

import nl.xanderdekeijzer.rides.data.CartData
import nl.xanderdekeijzer.rides.track.Track

class Train(
    val id: String,
    val track: Track,
    val carts: ArrayList<CartData> = arrayListOf()
) {
    constructor(id: Int, track: Track, carts: ArrayList<CartData> = arrayListOf()): this(id.toString(), track, carts)
}