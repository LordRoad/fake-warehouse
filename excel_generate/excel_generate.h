// excel_generate.h : main header file for the PROJECT_NAME application
//

#pragma once

#ifndef __AFXWIN_H__
	#error "include 'stdafx.h' before including this file for PCH"
#endif

#include "resource.h"		// main symbols


// Cexcel_generateApp:
// See excel_generate.cpp for the implementation of this class
//

class Cexcel_generateApp : public CWinApp
{
public:
	Cexcel_generateApp();

// Overrides
	public:
	virtual BOOL InitInstance();

// Implementation

	DECLARE_MESSAGE_MAP()
};

extern Cexcel_generateApp theApp;