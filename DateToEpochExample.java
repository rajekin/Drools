Option Explicit

Sub AutoLinkDataGeneric()
    Dim ws As Worksheet
    Dim srcCell As Range
    Dim token As String
    Dim tokens() As String
    Dim delim As String
    Dim targetWs As Worksheet
    Dim foundCell As Range
    Dim i As Long

    ' Change this delimiter as needed (default is pipe character)
    delim = "|"
    
    ' Turn off screen updating and automatic calculation to speed up processing
    Application.ScreenUpdating = False
    Application.Calculation = xlCalculationManual
    
    ' Loop through every worksheet in the workbook
    For Each ws In ThisWorkbook.Worksheets
        ' Loop through each cell in the used range of the current sheet
        For Each srcCell In ws.UsedRange
            ' Only process cells that are not empty, do not have errors, and are not formulas
            If Not IsEmpty(srcCell.Value) And Not IsError(srcCell.Value) And Not srcCell.HasFormula Then
                ' Split the cell text into tokens using the delimiter
                tokens = Split(CStr(srcCell.Value), delim)
                ' Process each token individually
                For i = LBound(tokens) To UBound(tokens)
                    token = Trim(tokens(i))
                    If token <> "" Then
                        ' Search all other worksheets for an exact match of the token in column A
                        For Each targetWs In ThisWorkbook.Worksheets
                            ' Skip the source sheet
                            If targetWs.Name <> ws.Name Then
                                Set foundCell = FindTokenInColumnA(targetWs, token)
                                If Not foundCell Is Nothing Then
                                    ' Create a hyperlink in the source cell pointing to the found cell
                                    CreateHyperlinkInCell srcCell, foundCell, token
                                    Exit For  ' Exit the inner loop after the first match is found
                                End If
                            End If
                        Next targetWs
                    End If
                Next i
            End If
        Next srcCell
    Next ws
    
    ' Restore application settings
    Application.Calculation = xlCalculationAutomatic
    Application.ScreenUpdating = True
    
    MsgBox "Data linking complete!", vbInformation
End Sub

Function FindTokenInColumnA(ws As Worksheet, token As String) As Range
    Dim rng As Range
    Dim cell As Range
    
    ' Use the used range’s column A to search for the token
    On Error Resume Next
    Set rng = ws.UsedRange.Columns(1)
    On Error GoTo 0
    
    If rng Is Nothing Then
        Set FindTokenInColumnA = Nothing
        Exit Function
    End If
    
    For Each cell In rng.Cells
        If Not IsError(cell.Value) Then
            ' Compare cell value (as a string) to the token
            If CStr(cell.Value) = token Then
                Set FindTokenInColumnA = cell
                Exit Function
            End If
        End If
    Next cell
    
    Set FindTokenInColumnA = Nothing
End Function

Sub CreateHyperlinkInCell(srcCell As Range, targetCell As Range, displayText As String)
    ' Delete any existing hyperlink from the source cell
    On Error Resume Next
    srcCell.Hyperlinks.Delete
    On Error GoTo 0
    
    ' Create a hyperlink in the source cell pointing to the target cell in its worksheet
    srcCell.Hyperlinks.Add Anchor:=srcCell, Address:="", _
        SubAddress:="'" & targetCell.Parent.Name & "'!" & targetCell.Address, _
        TextToDisplay:=displayText
End Sub
