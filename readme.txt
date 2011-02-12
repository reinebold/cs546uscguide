CS546 Assignment 2 Readme
Name:  James Reinebold
USCID:  9896978197
Email:  reinebol@usc.edu

UI Notes:

1.  The current user position is displayed as a white circle with a blinking blue dot inside it.

2.  To add a building, select "Add Building" from the options menu.
	Only valid building info will be accepted (invalid data will be ignored):
		- The building code must have a length equal to three characters.
			* it will be converted to all upper case letters.
		- The latitude / longitude must be valid doubles.

3.  To get directions, select "Input Destination" from the options menu.
	You will then be prompted to enter a destination building code.  The code entered will be converted to upper case letters.
		- Select "Get Text Directions" to display a list of buildings to follow to reach the destination.
			* If the building does not exist, then an error message will be displayed.
			* To return to the map press the options menu button.
		- Select "Get Visual Directions" to draw the route on the map.
			* Paths are initially drawn in green.
			* When a user reaches within 150 microdegrees of a line segment endpoint, then that segment will turn black.
			* If the building does not exist, then no new line segments will be drawn.
		- Requesting directions will clear all line segment overlays from the map.

4.  To zoom in press "I".  To zoom out press "O".  Click and drag to move around the map.

Known Issues:

1.  The building overlay feature is not implemented.

If you have any questions, please contact me at reinebol@usc.edu