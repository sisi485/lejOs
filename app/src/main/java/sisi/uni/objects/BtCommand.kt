package sisi.uni.objects


/**
 * @author Simon Schlätker
 *
 * This enum is used to represent bt commands and is also implemented on the client side.
 */
enum class BtCommand {
    //events
    OK,
    AUTOSTART,
    BTSTART,
    KALIBRIEREN,
    STOP,

    //bt drive
    FAHRE_VORWAERTS,
    FAHRE_RUECKWAERTS,
    FAHRE_LINKS,
    FAHRE_RECHTS,
    HALT,
    GESCHWINDIGKEIT_HOCH,
    GESCHINDIGKEIT_RUNTER,
    KILL
}