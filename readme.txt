CS546 Assignment 3 Readme
Names:  James Reinebold, Xing Xu
USCIDs:  9896978197, 9740333933
Emails:  reinebol@usc.edu, xingx@usc.edu

UI Notes:

1.  UI navigation is performed by using the "Menu" key.  The initial screen is a MapView.
    *  To view events, press Menu -> View Events.
        * Then select an event to view date and summary info.
        * Press Menu->Back to map to return to the main screen.
    *  To get directions, press Menu -> Find Building
        * Initially a list of Building Type is displayed.
        * As before, Menu -> Back to Map returns the user to the main screen.
        * Select a building type to get a list of all USC buildings sorted by closest distance to GPS coordinate.
        * Select a building to be presented with a list of options:
            * You can get text directions (a list as in assignment 1).
            * You can get visual directions (as in assignment 2).
            * You can get visual directions with Text to Speech overlay.
            * Press cancel to return to the main screen.

2.  While finding directions:
    * Current user location is displayed as a blue dot.
    * The path is initially displayed in green and turns black as legs of the journey are completed.
    * The direction guide is displayed as a Toast message.
        * If only one building is in the path, the direction finder points you to the destination.
        * Otherwise, the direction finder points you to the next building on the journey.
    * Zoom in by using the default zoom options.
        
3.  When the user stops the application for any reason, the overlays will be cleared.

4.  Only buildings that are listed in buildingdata.txt are listed in this application.

If you have any questions, please contact us at reinebol@usc.edu or xingx@usc.edu.