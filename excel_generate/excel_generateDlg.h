// excel_generateDlg.h : header file
//

#pragma once
#include "afxwin.h"
#include "file_generate.h"
#include "afxcmn.h"

enum CFileType
{
	FT_CSV = 1,
	FT_XLS,
	FT_TXT
};

class CExcelParas
{
public:
	CExcelParas(CFileGenFactory* pSS = NULL, int metricNum = 1, int attrNum = 1, long rowNum = 1, 
		double spacesPercent = 0.0, int spacesOneCell = 0):
	  m_pSS(pSS),m_metricNum(metricNum),m_attrNum(attrNum),m_rowNum(rowNum),
		m_spaces_onecell(spacesOneCell),m_spaces_percent(spacesPercent)
	{
	}
	
	CFileGenFactory* m_pSS;
	int m_metricNum;
	int m_attrNum;
	long m_rowNum;
	double m_spaces_percent;
	int m_spaces_onecell;
protected:
private:
};

class Cexcel_generateDlg;

class CStatusParas
{
public:
	CStatusParas(Cexcel_generateDlg* ptr = NULL, HANDLE* handle = NULL):
	  m_pDlg(ptr),m_handle(handle)
	{}
	Cexcel_generateDlg* m_pDlg;
	HANDLE* m_handle;
protected:
private:
};

// Cexcel_generateDlg dialog
class Cexcel_generateDlg : public CDialog
{
// Construction
public:
	Cexcel_generateDlg(CWnd* pParent = NULL);	// standard constructor
	virtual ~Cexcel_generateDlg();

// Dialog Data
	enum { IDD = IDD_EXCEL_GENERATE_DIALOG };

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV support


// Implementation
protected:
	HICON m_hIcon;

	// Generated message map functions
	virtual BOOL OnInitDialog();
	afx_msg void OnSysCommand(UINT nID, LPARAM lParam);
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();
	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnBnClickedOk();
	afx_msg void OnBnClickedTest();
private:
	// customer name
	CEdit m_cus_name;
public:
	// number of attributes
	CEdit m_num_attr;
	// number of metrics
	CEdit m_num_metrics;
private:
	// number of rows
	CEdit m_num_rows;
public:
	afx_msg void OnBnClickedRadioInput();
	afx_msg void OnBnClickedRadioRandom();
private:
	int m_random;
	CFileGenFactory* m_pSpreadSheet;
	CExcelParas m_ExcelPara;
	CStatusParas m_status;
public:
	// file type
	//CListCtrl m_list_types;
	CComboBox m_combo_types;
	int m_filetype;

private:
	static unsigned int __stdcall ThreadGenerateData(PVOID paras);
	static unsigned int __stdcall ThreadStatus(PVOID paras);

	static unsigned int __stdcall ThreadTestData(PVOID paras);
	static unsigned int __stdcall ThreadTestStatus(PVOID paras);

	HANDLE m_hexcel;
	HANDLE m_testprocess;
	HANDLE m_tstatus;
	HANDLE m_hstatus;
public:
	CButton m_csvbtn;
	CButton m_xlsBtn;
	CButton m_txtBtn;
	afx_msg void OnBnClickedCheckCsv();
	afx_msg void OnBnClickedCheckXls();
	afx_msg void OnBnClickedCheckTxt();
	CSliderCtrl m_spaces_percent;
	CEdit m_spaces_oncell;
	CButton m_genBtn;
	CButton m_testBtn;
};
