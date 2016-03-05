package com.helpermonkey.excel.macro;

/**
 * @author Vimal (Vimalasekar Rajendran; 131807)
 *
 */
public class MacroTemplate_ResourceNew {


	public MacroTemplate_ResourceNew() throws Exception {
	}
	

	/* *** All these macros are written in the worksheet --> Change event *** */
	
	/* *** Macro for setting the Change Flag to 'Y' whenever any row gets changed. *** */

//    Private Sub Worksheet_Change(ByVal Target As Range)
//    
//    Dim cellName As String
//    Dim j As Integer, cfind, c As Range, ws1, rscSheet As Worksheet
//    Dim opIdCol, opNmCol, chgFlgCol, pcustcol, projRoleCol As Integer
//    Dim srcIdCol, srcpcustid, srcpcustcol, srcopNmCol As Integer
//    Dim idCell, startRange, endRange, opptyNameList As String
//    Dim dv As Validation
//
//    Set rscSheet = ActiveSheet
//
//    Set MR = Range("A1:Z1")
//    For Each cell In MR
//        If cell.Value = "OpptyProjectId" Then
//          opIdCol = cell.Column
//        ElseIf cell.Value = "OpptyProjectName" Then
//          opNmCol = cell.Column
//        ElseIf cell.Value = "ChangeFlag" Then
//          chgFlgCol = cell.Column
//        ElseIf cell.Value = "ParentCustomerName" Then
//          pcustcol = cell.Column
//        ElseIf cell.Value = "ProjectRole" Then
//          projRoleCol = cell.Column
//        End If
//    Next
//
//    If ((rscSheet.Range(Cells(Target.Row, chgFlgCol).Address).Value <> "Y") And (Target.Row <> 1) And (rscSheet.Range(Cells(Target.Row, opNmCol).Address).Value <> Empty)) Then
//      rscSheet.Range(Cells(Target.Row, chgFlgCol).Address).Value = "Y"
//    End If
//    
//    If (Target.Column = pcustcol) Then
//        Set projectSheet = ThisWorkbook.Sheets("Projects")
//        
//        Set MR = projectSheet.Range("A1:Z1")
//        For Each cell In MR
//            If cell.Value = "OpptyProjectName" Then
//                srcopNmCol = cell.Column
//            ElseIf cell.Value = "ParentCustomerName" Then
//                srcpcustcol = cell.Column
//            End If
//        Next
//        
//        ' nullify the project name the moment parent customer is selected
//        rscSheet.Range(Cells(Target.Row, opNmCol).Address).Value = ""
//        
//    With projectSheet.UsedRange
//        Set c = .Find(what:=(rscSheet.Range(Cells(Target.Row, pcustcol).Address).Value), LookIn:=xlValues)
//        If Not c Is Nothing Then
//            firstAddress = c.Address
//            ' Start Range
//            opptyNameList = projectSheet.Range(Cells(c.Row, srcopNmCol).Address).Value
//            opptyNameList = opptyNameList & ","
//            Do
//                Set c = .FindNext(c)
//                If (c.Address <> firstAddress) Then
//                    opptyNameList = opptyNameList & projectSheet.Range(Cells(c.Row, srcopNmCol).Address).Value & ","
//                End If
//            Loop While Not c Is Nothing And c.Address <> firstAddress
//        End If
//    End With
//
//    Set dv = ActiveSheet.Range(Cells(Target.Row, opNmCol).Address).Validation
//
//    If (opptyNameList = "") Then
//      opptyNameList = "No Projects Available For Customer"
//    End If
//
//    dv.Delete
//    dv.Add xlValidateList, xlValidAlertStop, xlBetween, opptyNameList
//
//    End If
//
//    End Sub
//
//
//

	
}
