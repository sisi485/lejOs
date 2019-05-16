package sisi.uni.objects


/**
 * @author Simon Schlätker
 *
 * This enum is used to represent bt commands and is also implemented on the client side.
 */
enum class BtCommand {
    //events
    /**
     * ok signal used for internal nxt use
     */
    OK,
    /**
     * auto start signal to start auto mode
     */
    AUTOSTART,
    /**
     * bt start signal to start bt control mode
     */
    BTSTART,
    /**
     * calibrate signal to start calibration
     */
    KALIBRIEREN,
    /**
     * stop signal to stop current action
     */
    STOP,

    //bt drive
    /**
     * drive up signal
     */
    FAHRE_VORWAERTS,
    /**
     * drive downside signal
     */
    FAHRE_RUECKWAERTS,
    /**
     * drive left signal
     */
    FAHRE_LINKS,
    /**
     * drive right signal
     */
    FAHRE_RECHTS,
    /**
     * hold signal to hold position
     */
    HALT,
    /**
     * speed up signal to increment speed
     */
    GESCHWINDIGKEIT_HOCH,
    /**
     * slow down signal to decrement speed
     */
    GESCHINDIGKEIT_RUNTER,
    /**
     * kill signal to shut down the nxt
     */
    KILL
}