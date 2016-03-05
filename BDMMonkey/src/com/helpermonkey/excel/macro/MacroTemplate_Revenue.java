package com.helpermonkey.excel.macro;

/**
 * @author Vimal (Vimalasekar Rajendran; 131807)
 *
 */
public class MacroTemplate_Revenue {


	public MacroTemplate_Revenue() throws Exception {
	}
	

	/* *** All these macros are written in the worksheet --> Change event *** */
	
	/* *** Macro for setting the Change Flag to 'Y' whenever any row gets changed. *** */

//    Private Sub Worksheet_Change(ByVal Target As Range)
//
//    Dim cellName As String
//    Dim j As Integer, cfind As Range, ws1 As Worksheet
//    Dim custIdCol, custNmCol, chgFlgCol, srcCustIdCol, prjNmCol As Integer
//    Dim idCell As String
//
//    Set MR = Range("A1:Z1")
//    For Each cell In MR
//        If cell.Value = "CustomerId" Then
//          custIdCol = cell.Column
//        ElseIf cell.Value = "ParentCustomerName" Then
//          custNmCol = cell.Column
//        ElseIf cell.Value = "ChangeFlag" Then
//          chgFlgCol = cell.Column
//        ElseIf cell.Value = "OpptyProjectName" Then
//          prjNmCol = cell.Column
//        End If
//    Next
//
//    If ((ActiveSheet.Range(Cells(Target.Row, chgFlgCol).Address).Value <> "Y") And (Target.Row <> 1) And (ActiveSheet.Range(Cells(Target.Row, prjNmCol).Address).Value <> Empty)) Then
//      ActiveSheet.Range(Cells(Target.Row, chgFlgCol).Address).Value = "Y"
//    End If
//      
//    If (Target.Column = custNmCol) Then
//        Set ws = ThisWorkbook.Sheets("ParentCustomer")
//        Set cfind = ws.UsedRange.Cells.Find(what:=Target.Value, lookat:=xlWhole)
//        
//        Set MR = ws.Range("A1:Z1")
//        For Each cell In MR
//            If cell.Value = "Id" Then
//              srcCustIdCol = cell.Column
//            End If
//        Next
//
/*** removed this line and also the customer id column completely from excel
 *         ActiveSheet.Range(Cells(Target.Row, custIdCol).Address).Value = ws.Range(Cells(cfind.Row, srcCustIdCol).Address).Value
 */
//    End If
//    End Sub
//


}
