# ColorsTable
Designed an app which used HSV color model to define color gradient swatches and contains the database of colors for over more than 
1800 distinct colors. 

The idea behind this application was to allow the user to select and diplay color swatches in which colors will be classified as per their
Hue, Saturation and Value. The color swatches were displayed in fragment containg a ListView of colors. The application allows the users 
to select the values of Hue, Saturation and Values to see variation in the fragments of the colors. Also it allows the user to select a
particular color and make variations in that particular fragment. A SQLite database was implemented to store the color values 
as per the HSV values. Along with this one could limit the number of swatches that should be displayed in the fragment. 

Addition to this a configure color swatch is made in order to provide the user to view the change in color as the user slides through
the gradient in real-time. 

A unique feature is added in which a user can take a picture of a color and using content provider, the HSV values of the picture
taken was compared with the color values present in the database. If there is a match, it will provide the color otherwise would give
the best match possible from the database.
In order to extract the color from the image, the process was to iterate through the pixels of the image and retrieve 
the corresponding HSV values. 
