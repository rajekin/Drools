Subject: New VBA Tool for Easy Navigation in Test Results Excel Files

Hi Team,

I wanted to share a new generic VBA code that I’ve developed to improve our data linking process in Excel. This tool automatically creates hyperlinks between worksheets based on matching data. It’s designed to work in any Excel workbook and makes it very easy for business users to navigate between data points—especially when we email them the test results.

How It Works:
Automatic Linking:
The code loops through every worksheet in the workbook. For each cell in the used range, it splits the cell content using a specified delimiter (by default, the pipe symbol “|”) into separate tokens.
Data Search & Hyperlinking:
For each token, the code searches all other worksheets (specifically looking in column A) for an exact match. When a match is found, a hyperlink is created in the original cell that points directly to the matching cell.
User Benefit:
When business users receive the test results via email, they can click on these hyperlinks to jump directly to the relevant data. This makes reviewing and cross-referencing data much more intuitive and efficient.
How to Use the Code:
Open the Excel File:
Open any Excel workbook where you want to implement this feature.
Access the VBA Editor:
Press ALT + F11 to open the VBA editor.
Insert a New Module:
In the VBA editor, go to Insert > Module.
Copy & Paste the Code:
Copy the complete VBA code (provided below) and paste it into the new module.
Save the Workbook:
Save the file as a macro-enabled workbook (with an .xlsm extension).
Run the Macro:
Close the VBA editor, then run the macro named AutoLinkDataGeneric from Excel (via Developer > Macros or by pressing ALT + F8).
Below is the complete code:
