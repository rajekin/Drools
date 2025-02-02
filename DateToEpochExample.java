Option Explicit

'-----------------------------------------------------------
' Main routine: Scans every worksheet for tokens and creates
' bidirectional hyperlinks between matching cells.
'-----------------------------------------------------------
Sub AutoLinkDataGenericBiDirectional()
    Dim ws As Worksheet
    Dim srcCell As Range
    Dim token As String
    Dim tokens() As String
    Dim delim As String
    Dim targetWs As Worksheet
    Dim foundCell As Range
    Dim i As Long

    ' Set the delimiter for splitting cell text (change if needed)
    delim = "|"
    
    ' Turn off screen updating and automatic calculation to speed up processing.
    Application.ScreenUpdating = False
    Application.Calculation = xlCalculationManual
    
    ' Loop through every worksheet in the workbook.
    For Each ws In ThisWorkbook.Worksheets
        ' Loop through each cell in the used range of the current worksheet.
        For Each srcCell In ws.UsedRange
            ' Process only cells that are not empty, do not contain an error, and are not formulas.
            If Not IsEmpty(srcCell.Value) And Not IsError(srcCell.Value) And Not srcCell.HasFormula Then
                ' Split the cell text into tokens using the specified delimiter.
                tokens = Split(CStr(srcCell.Value), delim)
                For i = LBound(tokens) To UBound(tokens)
                    token = Trim(tokens(i))
                    If token <> "" Then
                        ' Search every other worksheet for a matching token in column A.
                        For Each targetWs In ThisWorkbook.Worksheets
                            If targetWs.Name <> ws.Name Then
                                Set foundCell = FindTokenInColumnA(targetWs, token)
                                If Not foundCell Is Nothing Then
                                    ' Create bidirectional hyperlinks using the token as display text in both cells.
                                    CreateBiDirectionalHyperlinks srcCell, foundCell, token
                                    Exit For  ' Exit after the first match is found.
                                End If
                            End If
                        Next targetWs
                    End If
                Next i
            End If
        Next srcCell
    Next ws
    
    ' Restore application settings.
    Application.Calculation = xlCalculationAutomatic
    Application.ScreenUpdating = True
    
    MsgBox "Bidirectional linking complete!", vbInformation
End Sub

'-----------------------------------------------------------
' Searches column A of a given worksheet for an exact match
' of the provided token (as a string) and returns the cell.
'-----------------------------------------------------------
Function FindTokenInColumnA(ws As Worksheet, token As String) As Range
    Dim rng As Range
    Dim cell As Range
    
    On Error Resume Next
    ' Limit the search to the used portion of column A.
    Set rng = ws.UsedRange.Columns(1)
    On Error GoTo 0
    
    If rng Is Nothing Then
        Set FindTokenInColumnA = Nothing
        Exit Function
    End If
    
    For Each cell In rng.Cells
        If Not IsError(cell.Value) Then
            ' Compare the cell value (converted to a string) with the token.
            If CStr(cell.Value) = token Then
                Set FindTokenInColumnA = cell
                Exit Function
            End If
        End If
    Next cell
    
    Set FindTokenInColumnA = Nothing
End Function

'-----------------------------------------------------------
' Creates a hyperlink in a cell pointing to a target cell.
'-----------------------------------------------------------
Sub CreateHyperlinkInCell(srcCell As Range, targetCell As Range, displayText As String)
    On Error Resume Next
    ' Delete any existing hyperlink in the cell.
    srcCell.Hyperlinks.Delete
    On Error GoTo 0
    
    ' Create a new hyperlink pointing to the target cell.
    srcCell.Hyperlinks.Add _
        Anchor:=srcCell, _
        Address:="", _
        SubAddress:="'" & targetCell.Parent.Name & "'!" & targetCell.Address, _
        TextToDisplay:=displayText
End Sub

'-----------------------------------------------------------
' Creates bidirectional hyperlinks between the source cell and
' the target cell using the provided token as display text.
'-----------------------------------------------------------
Sub CreateBiDirectionalHyperlinks(srcCell As Range, targetCell As Range, token As String)
    ' Create the forward hyperlink in the source cell.
    CreateHyperlinkInCell srcCell, targetCell, token
    ' Create the reverse hyperlink in the target cell (using the same token).
    CreateHyperlinkInCell targetCell, srcCell, token
End Sub
