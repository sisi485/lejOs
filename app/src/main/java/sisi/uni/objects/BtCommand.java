package sisi.uni.objects;

public enum BtCommand {
        //events
        AUTOSTART,
        BTSTART,
        KALIBRIEREN,
        STOP, //Zustanswechsel

        //Steuerbefehle fuer BTFahren
        FAHRE_VORWAERTS,
        FAHRE_RUECKWAERTS,
        FAHRE_LINKS,
        FAHRE_RECHTS,
        HALT,
        GESCHWINDIGKEIT_HOCH,
        GESCHINDIGKEIT_RUNTER
}
