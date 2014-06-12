// excel_generateDlg.cpp : implementation file
//

#include "stdafx.h"
#include "excel_generate.h"
#include "excel_generateDlg.h"
#include "CSpreadSheet.h"
#include "csv_generate.h"
#include "auto.h"

#include <iostream>
#include <string>
#include <Windows.h>
#include <process.h>

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CAboutDlg dialog used for App About

class CAboutDlg : public CDialog
{
public:
	CAboutDlg();

// Dialog Data
	enum { IDD = IDD_ABOUTBOX };

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

// Implementation
protected:
	DECLARE_MESSAGE_MAP()
};

CAboutDlg::CAboutDlg() : CDialog(CAboutDlg::IDD)
{
}

void CAboutDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}

BEGIN_MESSAGE_MAP(CAboutDlg, CDialog)
END_MESSAGE_MAP()


// Cexcel_generateDlg dialog


Cexcel_generateDlg::Cexcel_generateDlg(CWnd* pParent /*=NULL*/)
	: CDialog(Cexcel_generateDlg::IDD, pParent)
	, m_random(0)
{
	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);
	m_pSpreadSheet = NULL;
	m_filetype = FT_CSV;
}

Cexcel_generateDlg::~Cexcel_generateDlg()
{
	if (m_pSpreadSheet)
	{
		delete m_pSpreadSheet;
	}
	m_pSpreadSheet = NULL;
}

void Cexcel_generateDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_CUS_NAME, m_cus_name);
	DDX_Control(pDX, IDC_NUM_ATTR, m_num_attr);
	DDX_Control(pDX, IDC_NUM_METRIC, m_num_metrics);
	DDX_Control(pDX, IDC_NUM_ROWS, m_num_rows);
	//DDX_Control(pDX, IDC_LIST_TYPE, m_list_types);
	DDX_Control(pDX, IDC_COMBO1, m_combo_types);
	DDX_Control(pDX, IDC_CHECK_CSV, m_csvbtn);
	DDX_Control(pDX, IDC_CHECK_XLS, m_xlsBtn);
	DDX_Control(pDX, IDC_CHECK_TXT, m_txtBtn);
	DDX_Control(pDX, IDC_SLIDER_SPACES_PER, m_spaces_percent);
	DDX_Control(pDX, IDC_EDIT_SPACES_NUM_ONECELL, m_spaces_oncell);
	DDX_Control(pDX, IDOK, m_genBtn);
	DDX_Control(pDX, IDOK_TESTING, m_testBtn);
}

BEGIN_MESSAGE_MAP(Cexcel_generateDlg, CDialog)
	ON_WM_SYSCOMMAND()
	ON_WM_PAINT()
	ON_WM_QUERYDRAGICON()
	//}}AFX_MSG_MAP
	ON_BN_CLICKED(IDOK, &Cexcel_generateDlg::OnBnClickedOk)
	ON_BN_CLICKED(IDC_RADIO_INPUT, &Cexcel_generateDlg::OnBnClickedRadioInput)
	ON_BN_CLICKED(IDC_RADIO_RANDOM, &Cexcel_generateDlg::OnBnClickedRadioRandom)
	ON_BN_CLICKED(IDC_CHECK_CSV, &Cexcel_generateDlg::OnBnClickedCheckCsv)
	ON_BN_CLICKED(IDC_CHECK_XLS, &Cexcel_generateDlg::OnBnClickedCheckXls)
	ON_BN_CLICKED(IDC_CHECK_TXT, &Cexcel_generateDlg::OnBnClickedCheckTxt)
	ON_BN_CLICKED(IDOK_TESTING, &Cexcel_generateDlg::OnBnClickedTest)
END_MESSAGE_MAP()


// Cexcel_generateDlg message handlers

BOOL Cexcel_generateDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// Add "About..." menu item to system menu.

	// IDM_ABOUTBOX must be in the system command range.
	ASSERT((IDM_ABOUTBOX & 0xFFF0) == IDM_ABOUTBOX);
	ASSERT(IDM_ABOUTBOX < 0xF000);

	CMenu* pSysMenu = GetSystemMenu(FALSE);
	if (pSysMenu != NULL)
	{
		CString strAboutMenu;
		strAboutMenu.LoadString(IDS_ABOUTBOX);
		if (!strAboutMenu.IsEmpty())
		{
			pSysMenu->AppendMenu(MF_SEPARATOR);
			pSysMenu->AppendMenu(MF_STRING, IDM_ABOUTBOX, strAboutMenu);
		}
	}

	// Set the icon for this dialog.  The framework does this automatically
	//  when the application's main window is not a dialog
	SetIcon(m_hIcon, TRUE);			// Set big icon
	SetIcon(m_hIcon, FALSE);		// Set small icon

	// TODO: Add extra initialization here
	m_cus_name.EnableWindow(FALSE);
	((CButton*)GetDlgItem(IDC_RADIO_RANDOM))->SetCheck(TRUE);
	((CButton*)GetDlgItem(IDC_RADIO_INPUT))->SetCheck(FALSE);

	//m_list_types.InsertColumn(0, ".csv", LVCFMT_CENTER);
	//m_list_types.InsertColumn(1, ".xls", LVCFMT_CENTER);
	//m_list_types.InsertColumn(2, ".xlsx", LVCFMT_CENTER);
	//m_combo_types.ResetContent();
	m_combo_types.EnableScrollBar(SB_BOTH);
	//m_combo_types.EnableWindow(TRUE);
	m_combo_types.InsertString(0, ".csv");
	m_combo_types.InsertString(1, ".xls");
	m_combo_types.InsertString(2, ".xlsx");
	m_combo_types.SetCurSel(0);
	//m_combo_types.ShowDropDown(TRUE);


	return TRUE;  // return TRUE  unless you set the focus to a control
}

void Cexcel_generateDlg::OnSysCommand(UINT nID, LPARAM lParam)
{
	if ((nID & 0xFFF0) == IDM_ABOUTBOX)
	{
		CAboutDlg dlgAbout;
		dlgAbout.DoModal();
	}
	else
	{
		CDialog::OnSysCommand(nID, lParam);
	}
}

// If you add a minimize button to your dialog, you will need the code below
//  to draw the icon.  For MFC applications using the document/view model,
//  this is automatically done for you by the framework.

void Cexcel_generateDlg::OnPaint()
{
	if (IsIconic())
	{
		CPaintDC dc(this); // device context for painting

		SendMessage(WM_ICONERASEBKGND, reinterpret_cast<WPARAM>(dc.GetSafeHdc()), 0);

		// Center icon in client rectangle
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;

		// Draw the icon
		dc.DrawIcon(x, y, m_hIcon);
	}
	else
	{
		CDialog::OnPaint();
	}
}

// The system calls this function to obtain the cursor to display while the user drags
//  the minimized window.
HCURSOR Cexcel_generateDlg::OnQueryDragIcon()
{
	return static_cast<HCURSOR>(m_hIcon);
}

 unsigned int __stdcall Cexcel_generateDlg::ThreadGenerateData(PVOID paras)
 {
	CExcelParas* pExcelParas= (CExcelParas*)paras;
	
	int attrnum = pExcelParas->m_attrNum;
	int metricnum = pExcelParas->m_metricNum;
	long rownum = pExcelParas->m_rowNum;
	long rowsWithSpaces = rownum * pExcelParas->m_spaces_percent;
	long rowsWithoutSpaces = rownum - rowsWithSpaces;

	CFileGenFactory* spreadsheet = pExcelParas->m_pSS;
	
	CStringArray StrHeaderArray;
	int iter = 0;
	CString strCompund;
	std::vector<int> datatype;
	for (iter=1; iter <= attrnum; ++iter)
	{
		strCompund.Format("attribute %d", iter);
		StrHeaderArray.Add(strCompund);
		datatype.push_back(DSTRING);
	}
	for (iter=1; iter <= metricnum; ++iter)
	{
		strCompund.Format("metric %d", iter);
		StrHeaderArray.Add(strCompund);
		datatype.push_back(DDOUBLE);
	}
	//spreadsheet->BeginTransaction();
	
	spreadsheet->AddHeaders(StrHeaderArray, &datatype, false);
	
	const int iEachloop = 100;
	CStringArray StrDataArray;
	long currow = 1;
	srand(1);

	// rows with spaces
	CString strSpaces;
	for (iter = 0; iter < pExcelParas->m_spaces_onecell; ++iter)
	{
		strSpaces += " ";
	}
	int itimes = (int)(rowsWithSpaces / iEachloop);
	while(itimes-- > 0)
	{
		spreadsheet->BeginTransaction();
		for (int jter = 0; jter < iEachloop; ++jter)
		{
			// generate data
			StrDataArray.RemoveAll();
			for (iter=1; iter <= attrnum; ++iter)
			{
				strCompund.Format("%sattr %d", strSpaces.GetBuffer(),rand());
				StrDataArray.Add(strCompund);
			}
			for (iter=1; iter <= metricnum; ++iter)
			{
				strCompund.Format("%s%d", strSpaces.GetBuffer(),rand());
				StrDataArray.Add(strCompund);
			}
			spreadsheet->AddRow(StrDataArray, ++currow);
		}
		spreadsheet->Commit();
	}

	int irest = rowsWithSpaces % iEachloop;
	if (irest > 0)
	{
		spreadsheet->BeginTransaction();
		for (int jter = 0; jter < irest; ++jter)
		{
			// generate data
			StrDataArray.RemoveAll();
			for (iter=1; iter <= attrnum; ++iter)
			{
				strCompund.Format("%sattr %d", strSpaces.GetBuffer(),rand());
				StrDataArray.Add(strCompund);
			}
			for (iter=1; iter <= metricnum; ++iter)
			{
				strCompund.Format("%s%d", strSpaces.GetBuffer(),rand());
				StrDataArray.Add(strCompund);
			}
			spreadsheet->AddRow(StrDataArray, ++currow);
		}
		spreadsheet->Commit();
	}
	//spreadsheet->Commit();
	//CString strerr = spreadsheet->GetLastError();
	
	// row without spaces
	int itimes1 = (int)(rowsWithoutSpaces / iEachloop);
	while(itimes1-- > 0)
	{
		spreadsheet->BeginTransaction();
		for (int jter = 0; jter < iEachloop; ++jter)
		{
			// generate data
			StrDataArray.RemoveAll();
			for (iter=1; iter <= attrnum; ++iter)
			{
				strCompund.Format("attr %d", rand());
				StrDataArray.Add(strCompund);
			}
			for (iter=1; iter <= metricnum; ++iter)
			{
				strCompund.Format("%d", rand());
				StrDataArray.Add(strCompund);
			}
			spreadsheet->AddRow(StrDataArray, ++currow);
		}
		spreadsheet->Commit();
	}
	int irest1 = rowsWithoutSpaces % iEachloop;
	if (irest1 > 0)
	{
		spreadsheet->BeginTransaction();
		for (int jter = 0; jter < irest1; ++jter)
		{
			// generate data
			StrDataArray.RemoveAll();
			for (iter=1; iter <= attrnum; ++iter)
			{
				strCompund.Format("attr %d", rand());
				StrDataArray.Add(strCompund);
			}
			for (iter=1; iter <= metricnum; ++iter)
			{
				strCompund.Format("%d", rand());
				StrDataArray.Add(strCompund);
			}
			spreadsheet->AddRow(StrDataArray, ++currow);
		}
		spreadsheet->Commit();
	}

	spreadsheet->Close();

	return 1;
 }

 unsigned int __stdcall Cexcel_generateDlg::ThreadStatus(PVOID paras)
 {
	 CStatusParas* statuspara = (CStatusParas*)paras;
	 HANDLE hexcel = *(statuspara->m_handle);

	DWORD dwRet = WaitForSingleObject(hexcel, INFINITE);
	
	::MessageBox(NULL, "mission is down", "finish", MB_OK);

	statuspara->m_pDlg->m_testBtn.EnableWindow(TRUE);

	return 1;
 }

void Cexcel_generateDlg::OnBnClickedOk()
{
	// TODO: Add your control notification handler code here
	m_testBtn.EnableWindow(FALSE);
	UpdateData();

	const int iMaxDirLen = 256;
	AutoPtr<char> curdir(new char[iMaxDirLen]);
	GetCurrentDirectory(iMaxDirLen, curdir.GetPtr());
	std::string filestr = curdir.GetPtr();
	filestr += '\\';

	CString strFN;
	CString strAttrNum, strMetricNum, strRowNum;
	m_cus_name.GetWindowText(strFN);
	m_num_attr.GetWindowText(strAttrNum);
	m_num_metrics.GetWindowText(strMetricNum);
	m_num_rows.GetWindowText(strRowNum);
	
	m_ExcelPara.m_attrNum = atoi(strAttrNum.GetString());
	m_ExcelPara.m_metricNum = atoi(strMetricNum.GetString());
	m_ExcelPara.m_rowNum = atol(strRowNum.GetString());
	
	m_ExcelPara.m_spaces_percent = ((double)m_spaces_percent.GetPos() * m_spaces_percent.GetLineSize()) / 100.00;
	CString strSpacesOneCell;
	m_spaces_oncell.GetWindowText(strSpacesOneCell);
	m_ExcelPara.m_spaces_onecell = atoi(strSpacesOneCell.GetString());

	if (strFN.IsEmpty())
	{
		SYSTEMTIME st;
		::GetSystemTime(&st);
		strFN.Format("%d-%d-%d-%d-%d-%d-%d-%ld-%d-%4f", st.wMonth, st.wDay, st.wHour, st.wMinute, st.wSecond, 
			m_ExcelPara.m_attrNum, m_ExcelPara.m_metricNum, m_ExcelPara.m_rowNum, m_ExcelPara.m_spaces_onecell,
			m_ExcelPara.m_spaces_percent);
	}
	// now i have no idea about the ComboBox, ok ,take it as CSV.
	switch(m_filetype)
	{
	case FT_CSV:
		{
			strFN += L".csv";
			break;
		}
	case FT_TXT:
		{
			strFN += L".txt";
			break;
		}
	case FT_XLS:
		{
			strFN += L".xls";
			break;
		}
	}
	//AutoPtr<char> filename(new char[strFN.GetLength()+1]);
	//::wcstombs(filename.GetPtr(), strFN.GetBuffer(), strFN.GetLength()+1);
	//filestr += filename.GetPtr();
	filestr += strFN;
	
	try
	{
		switch(m_filetype)
		{
		case FT_TXT:
		case FT_CSV:
			{
				m_pSpreadSheet = new CCSV_Generate(filestr.c_str());
				break;
			}
	/*	case FT_TXT:
			{
				::MessageBox(NULL, "not provided now", "warning", MB_OK);
				return;
			}*/
		case FT_XLS:
			{
				m_pSpreadSheet = new CSpreadSheet(filestr.c_str(), "Sheet1", false);
				break;
			}
		}
		
		//CCSV_Generate test(strFN);
		m_ExcelPara.m_pSS = m_pSpreadSheet;
	}
	catch (...)
	{
		return;
	}
	
	// i don't check these stuff, oh , i know i'm doing bad programming
	m_hexcel = (HANDLE)_beginthreadex(
		NULL,
		0,
		&Cexcel_generateDlg::ThreadGenerateData,
		(PVOID)&m_ExcelPara,
		0,
		NULL);
	m_status.m_pDlg = this;
	m_status.m_handle = &m_hexcel;
	m_hstatus = (HANDLE)_beginthreadex(
		NULL,
		0,
		&Cexcel_generateDlg::ThreadStatus,
		(PVOID)&m_status,
		0,
		NULL);

}

void Cexcel_generateDlg::OnBnClickedRadioInput()
{
	// TODO: Add your control notification handler code here
	m_cus_name.EnableWindow(TRUE);
	((CButton*)GetDlgItem(IDC_RADIO_RANDOM))->SetCheck(FALSE);
	((CButton*)GetDlgItem(IDC_RADIO_INPUT))->SetCheck(TRUE);

}

void Cexcel_generateDlg::OnBnClickedRadioRandom()
{
	// TODO: Add your control notification handler code here
	((CButton*)GetDlgItem(IDC_RADIO_RANDOM))->SetCheck(TRUE);
	((CButton*)GetDlgItem(IDC_RADIO_INPUT))->SetCheck(FALSE);
	m_cus_name.EnableWindow(FALSE);
}

void Cexcel_generateDlg::OnBnClickedCheckCsv()
{
	// TODO: Add your control notification handler code here
	if (m_csvbtn.GetCheck())
	{
		m_filetype = FT_CSV;
		m_xlsBtn.EnableWindow(FALSE);
		m_txtBtn.EnableWindow(FALSE);
	}
	else
	{
		m_xlsBtn.EnableWindow(TRUE);
		m_txtBtn.EnableWindow(TRUE);
	}
}

void Cexcel_generateDlg::OnBnClickedCheckXls()
{
	// TODO: Add your control notification handler code here
	if (m_xlsBtn.GetCheck())
	{
		m_filetype = FT_XLS;
		m_csvbtn.EnableWindow(FALSE);
		m_txtBtn.EnableWindow(FALSE);
	}
	else
	{
		m_csvbtn.EnableWindow(TRUE);
		m_txtBtn.EnableWindow(TRUE);
	}
}

void Cexcel_generateDlg::OnBnClickedCheckTxt()
{
	// TODO: Add your control notification handler code here
	if (m_txtBtn.GetCheck())
	{
		m_filetype = FT_TXT;
		m_xlsBtn.EnableWindow(FALSE);
		m_csvbtn.EnableWindow(FALSE);
	}
	else
	{
		m_xlsBtn.EnableWindow(TRUE);
		m_csvbtn.EnableWindow(TRUE);
	}
}

unsigned int __stdcall Cexcel_generateDlg::ThreadTestData(PVOID paras)
{
	const char* yatipath = "Z:\\BIN\\yati_ut.exe ";
	const char* yatiparas = "Z:\\BIN\\MJDataImportTest.dll CSVFileImportTest";

	std::vector<char*> testcases;
	testcases.push_back("1024");
	testcases.push_back("8192");
	testcases.push_back("65536");
	testcases.push_back("524288");
	testcases.push_back("4194304");
	
	int res = 0;
	for (int iter = 0; iter < testcases.size(); ++iter)
	{
		std::string cmd = yatipath;
		cmd += yatiparas;
		cmd += testcases[iter];
		for (int loops = 0; loops < 10; ++loops)
		{
			res = system(cmd.c_str());
		}
	}

	return 1;
}

unsigned int __stdcall Cexcel_generateDlg::ThreadTestStatus(PVOID paras)
{
	CStatusParas* statuspara = (CStatusParas*)paras;
	HANDLE testprocess = *(statuspara->m_handle);

	DWORD dwRet = WaitForSingleObject(testprocess, INFINITE);

	::MessageBox(NULL, "testing process is down", "finish", MB_OK);

	statuspara->m_pDlg->m_genBtn.EnableWindow(TRUE);

	return 1;	
}

void Cexcel_generateDlg::OnBnClickedTest()
{
	m_genBtn.EnableWindow(FALSE);
	m_testprocess = (HANDLE)_beginthreadex(
		NULL,
		0,
		&Cexcel_generateDlg::ThreadTestData,
		NULL,
		0,
		NULL);
	m_status.m_pDlg = this;
	m_status.m_handle = &m_testprocess;
	m_tstatus = (HANDLE)_beginthreadex(
		NULL,
		0,
		&Cexcel_generateDlg::ThreadTestStatus,
		(PVOID)&m_status,
		0,
		NULL);

}