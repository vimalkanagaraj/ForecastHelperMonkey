package com.helpermonkey.excel.macro;

/**
 * @author Vimal (Vimalasekar Rajendran; 131807)
 *
 */
public class MacroTemplate_Resource {


	public MacroTemplate_Resource() throws Exception {
	}
	

	/* *** All these macros are written in the worksheet --> Change event *** */
	
	/* *** Macro for setting the Change Flag to 'Y' whenever any row gets changed. *** */

//	Private Sub Worksheet_Change(ByVal Target As Range)
//
//	Dim cellName As String
//	Dim j As Integer, cfind, c As Range, ws1, rscSheet As Worksheet
//	Dim opIdCol, opNmCol, chgFlgCol, pcustcol, srcIdCol, srcpcustid, projRoleCol As Integer
//	Dim idCell, startRange, endRange, prjRoleList As String
//	Dim dv As Validation
//
//	Set rscSheet = ActiveSheet
//
//	Set MR = Range("A1:Z1")
//	For Each cell In MR
//	    If cell.Value = "OpptyProjectId" Then
//	      opIdCol = cell.Column
//	    ElseIf cell.Value = "OpptyProjectName" Then
//	      opNmCol = cell.Column
//	    ElseIf cell.Value = "ChangeFlag" Then
//	      chgFlgCol = cell.Column
//	    ElseIf cell.Value = "ParentCustomerName" Then
//	      pcustcol = cell.Column
//	    ElseIf cell.Value = "ProjectRole" Then
//	      projRoleCol = cell.Column
//	    End If
//	Next
//
//	If ((rscSheet.Range(Cells(Target.Row, chgFlgCol).Address).Value <> "Y") And (Target.Row <> 1) And (rscSheet.Range(Cells(Target.Row, opNmCol).Address).Value <> Empty)) Then
//	  rscSheet.Range(Cells(Target.Row, chgFlgCol).Address).Value = "Y"
//	End If
//	    
//	If (Target.Column = opNmCol) Then
//	    Set ws = ThisWorkbook.Sheets("Projects")
//	    Set cfind = ws.UsedRange.Cells.Find(what:=Target.Value, lookat:=xlWhole)
//	    
//	    Set MR = ws.Range("A1:Z1")
//	    For Each cell In MR
//	        If cell.Value = "Id" Then
//	          srcIdCol = cell.Column
//	        ElseIf cell.Value = "ParentCustomerName" Then
//	          srcpcustid = cell.Column
//	        End If
//	    Next
//
//	' set the project id from revenue sheet into the resource sheet as well
//	    ActiveSheet.Range(Cells(Target.Row, opIdCol).Address).Value = ws.Range(Cells(cfind.Row, srcIdCol).Address).Value
//	' set the customer id from revenue sheet into the resource sheet as well to aid updation of the lists independantly
//	    ActiveSheet.Range(Cells(Target.Row, pcustcol).Address).Value = ws.Range(Cells(cfind.Row, srcpcustid).Address).Value
//	    
//	Set ws1 = ThisWorkbook.Sheets("RateCard")
//	With ws1.UsedRange
//	    Set c = .Find(what:=(rscSheet.Range(Cells(Target.Row, pcustcol).Address).Value), LookIn:=xlValues)
//	    If Not c Is Nothing Then
//	        firstAddress = c.Address
//	        startRange = "B" & c.Row
//	        prjRoleList = ws1.Range(startRange).Value
//	        prjRoleList = prjRoleList & ","
//	        Do
//	            Set c = .FindNext(c)
//	            endRange = "B" & c.Row
//	            If (c.Address <> firstAddress) Then
//	                prjRoleList = prjRoleList & ws1.Range(endRange).Value & ","
//	            End If
//	        Loop While Not c Is Nothing And c.Address <> firstAddress
//	    End If
//	End With
//
//	Set dv = ActiveSheet.Range(Cells(Target.Row, projRoleCol).Address).Validation
//
//	If (prjRoleList = "") Then
//	  prjRoleList = "No Roles Available For Customer"
//	End If
//
//	dv.Delete
//	dv.Add xlValidateList, xlValidAlertStop, xlBetween, prjRoleList
//
//	' ThisWorkbook.Names.Add Name:="test_list", RefersTo:=ws1.Range(startRange, endRange)
//
//	End If
//
//	End Sub


	
}
