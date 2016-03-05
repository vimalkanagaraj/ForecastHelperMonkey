package com.helpermonkey.excel.macro;

/**
 * @author Vimal (Vimalasekar Rajendran; 131807)
 *
 */
public class MacroTemplate_RunJavaFromExcel {


	public MacroTemplate_RunJavaFromExcel() throws Exception {
	}
	
// you can  use
//	Shell("java.exe -jar " & <yourJARFile>)
// or shellexecute	
//
	
					/* *** another shell option *** */
	
	
//	Public Sub Shell_GetAll()
//    Dim oShell
//    Set oShell = CreateObject("WScript.Shell")
//    ' Set oShell = WScript.CreateObject("WScript.Shell")
//    ' Set Shell = CreateObject("WScript.Shell")
//    ' Dim JavaHome As String
//    ' JavaHome = Shell.Environment("USER")("JAVA_HOME") & "\"
//    cmd = JavaHome & "java -Xms128m -Xmx1024m -jar ""D:\\eclipseMars\\vimal_workspace\\deploy\\NumbersHelperMonkey.jar"" getAll"
//    Set objExecObject = oShell.exec(cmd)
//    ' oShell.Run "cmd /K CD C:\ & Dir"
//    Set oShell = Nothing
//    
//End Sub


					/* ***  this is a whole new code alternate to using shell command above *** */

	
//' add a reference to Windows Script Host Object Model, if not this code will not work
//' Click on your workbook in the Project window, choose Tools|References,
//' scroll down to Windows Script Host Object Model, check it and click OK.
//Option Explicit
//Private Declare Sub Sleep Lib "kernel32" (ByVal dwMilliseconds As Long)
//Public Sub Run_GetAll()
//    Dim program As WshExec
//    Set program = RunProgram("java -Xms128m -Xmx1024m -jar ""D:\\eclipseMars\\vimal_workspace\\deploy\\NumbersHelperMonkey.jar"" getAll")
//    Debug.Print "STDOUT: " & program.StdOut.ReadAll
//
//End Sub
//
//Private Sub RunSleep(exec As WshExec, Optional timeSegment As Long = 20)
//    Do While exec.Status = WshRunning
//        Sleep timeSegment
//    Loop
//End Sub
//
//Private Function RunProgram(program As String, Optional command As String = "") As WshExec
//    Dim wsh As New WshShell
//    Dim exec As WshExec
//
//    Set exec = wsh.exec(program)
//    Call exec.StdIn.WriteLine(command)
//    Call RunSleep(exec)
//    Set RunProgram = exec
//End Function
//

	
	

	
}
