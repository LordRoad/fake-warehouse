#include "stdafx.h"
#include "csv_generate.h"

#include <windows.h>

CCSV_Generate::CCSV_Generate(CString strFileName)
{
	m_curRow = 0;
	m_totalRow = 0;
	m_commit = true;
	
	m_filename = strFileName;
	//if(CoInitialize(NULL) != S_OK)  
	//{
	//	// error
	//}   
	m_status = begin_csv(m_filename);
}

CCSV_Generate::~CCSV_Generate()
{
	/*m_pWorkbook->Close(VARIANT_TRUE);
	m_pExcelApp->Quit();
	CoUninitialize();*/
	if (m_fileCreator)
	{
		m_fileCreator.Close();
	}

}

bool CCSV_Generate::begin_csv(CString strName)
{
	try
	{
		if (!m_fileCreator.Open(strName.GetBuffer(), CFile::modeCreate | CFile::modeReadWrite))
		{
			return false;
		}

		//HRESULT hr = m_pExcelApp.CreateInstance(L"Excel.Application");
		//assert(SUCCEEDED(hr));
		//m_pExcelApp->Visible = false;
		//
		//_variant_t vtOption((long)DISP_E_PARAMNOTFOUND, VT_ERROR);

		//
		//m_pWorkbook = m_pExcelApp->Workbooks->Open(strName.GetBuffer(), vtMissing
		//	, vtMissing, vtMissing, vtMissing, vtMissing, vtMissing, vtMissing, vtMissing, vtMissing,
		//	vtMissing, vtMissing, vtMissing, vtMissing, vtMissing); 
		////Excel::_WorkbookPtr pWorkbook = pExcelApp->GetWorkbooks();
		//if (!m_pWorkbook)
		//{
		//	return false;
		//}
		//m_pWorksheet = m_pWorkbook->ActiveSheet;
		//m_pWorksheet->Name = L"Sheet1";
		//m_pRange = m_pWorksheet->Cells;
			
		//m_pRange->GetEntireRow();

	/*	for (int i=0;i<nplot;++i)
		{
			double x = xlow+i*h;
			pRange->Item[i+2][1] = x;
			pRange->Item[i+2][2] = sin(x)*exp(-x);
		}*/

		//pWorkbook->Close(VARIANT_TRUE);  // save changes
		//pExcelApp->Quit();
	}
	catch (...)
	{
		DWORD dwErr = GetLastError();
		//::MessageBox(NULL, error.Description(), "CSV Error", MB_OK);
		return false;
	}
	return true;
}

void CCSV_Generate::Close()
{
	end_csv();
}

void CCSV_Generate::end_csv()
{
	//m_pWorkbook->Close(VARIANT_TRUE);
	//m_pExcelApp->Quit();
	m_fileCreator.Close();
}

/* set some paras for file generation */
bool CCSV_Generate::InitFile(PVOID paras)
{

	return true;
}

/* add table header */
bool CCSV_Generate::AddHeaders(CStringArray &FieldNames, std::vector<int>* pDatatype, bool replace)
{
	if (!m_status)
	{
		return false;
	}
	m_datatype = *pDatatype;
	
	return AddRow(FieldNames, 1);
}

/* add a row into table */
bool CCSV_Generate::AddRow(CStringArray &RowValues, long row, bool replace)
{
	if (RowValues.GetSize() < 1 || row < 0 || !m_status)
	{
		return false;
	}
	int rownum = row<=0 ? m_curRow : (row-1);
	CString strValue = "";
		for (int iter = 0; iter < RowValues.GetSize(); ++iter)
		{
			//strValue = RowValues.GetAt(iter);
			switch (m_datatype[iter])
			{
			case DINT:
				{
					//m_pRange->Item[rownum][iter] = _variant_t(atoi(strValue.GetBuffer()));
					strValue += RowValues.GetAt(iter) + ",";
					break;
				}
			case DDOUBLE:
				{
					strValue += RowValues.GetAt(iter) + ",";
					//m_pRange->Item[rownum][iter] = _variant_t(atof(strValue.GetBuffer()));
					break;
				}
			case DDATE:
				{
					strValue += RowValues.GetAt(iter) + ",";
					//break;
				}
			case DSTRING:
			default:
				{
					strValue += "\"" + RowValues.GetAt(iter) + "\",";
					//m_pRange->Item[rownum][iter] = _variant_t(strValue.GetBuffer());
				}
			}
			//strValue = RowValues.GetAt(iter) + ",";
		}
	//strValue.SetLength(strValue.GetLength()-1);
	strValue += "\n";
	if (m_commit)
	{
		m_fileCreator.Write(strValue.GetBuffer(), strValue.GetLength());
	}
	else
	{
		m_rows.Add(strValue);
	}
	++m_totalRow;
	m_curRow = rownum;
	return true;
}

bool CCSV_Generate::AddRows(CStringArray &RowValues, long len, long beginAt, bool replace)
{
	if (!m_status)
	{
		return false;
	}
	if (len > 0)
	{
		int pos = 0, jter;
		if (beginAt > 0)
		{
			m_curRow = beginAt; 
		}
		CString strValue;
		for (jter = m_curRow; jter < beginAt + len; ++jter)
		{
			strValue = "";
			for (int iter = 0; iter < RowValues.GetSize(); ++iter)
			{
				//strValue = RowValues.GetAt(pos++);
				switch (m_datatype[iter])
				{
				case DINT:
					{
						strValue += RowValues.GetAt(pos++) + ",";
						//m_pRange->Item[jter][iter] = atoi(strValue.GetBuffer());
						break;
					}
				case DDOUBLE:
					{
						strValue += RowValues.GetAt(pos++) + ",";
						//m_pRange->Item[jter][iter] = atof(strValue.GetBuffer());
						break;
					}
				case DDATE:
					{
						strValue += RowValues.GetAt(pos++) + ",";
						//break;
					}
				case DSTRING:
				default:
					{
						strValue += "\"" + RowValues.GetAt(pos++) + "\",";
						//m_pRange->Item[jter][iter] = strValue.GetBuffer();
					}
				}
			}
			strValue += "\n";
			if (m_commit)
			{
				m_fileCreator.Write(strValue.GetBuffer(), strValue.GetLength());
			}
			else
			{
				m_rows.Add(strValue);
			}
		}
		m_curRow = jter;
		return true;
	}

	return false;
}

/* will not execute SQL until Commit */
void CCSV_Generate::BeginTransaction()
{
	/* 
	* as for CSV, why adding begintransaction?
	* i will not consider the save process now.
	*/
	//m_commit = true;
	//if (!m_pWorkbook)
	//{
	//	m_pWorkbook = m_pExcelApp->Workbooks->Open(m_filename.GetBuffer(), vtMissing
	//		, vtMissing, vtMissing, vtMissing, vtMissing, vtMissing, vtMissing, vtMissing, vtMissing,
	//		vtMissing, vtMissing, vtMissing, vtMissing, vtMissing); 
	//	//Excel::_WorkbookPtr pWorkbook = pExcelApp->GetWorkbooks();
	//	if (!m_pWorkbook)
	//	{
	//	
	//	}
	//	m_pWorksheet = m_pWorkbook->ActiveSheet;
	//	//m_pWorksheet->Name = L"Sheet1";
	//	m_pRange = m_pWorksheet->Cells;
	//}

	m_commit = false;
}

bool CCSV_Generate::Commit()
{
	//m_pWorkbook->Close(VARIANT_TRUE);
	//m_pWorkbook = NULL;
	//m_commit = false;
	try
	{
		for (int iter = 0; iter < m_rows.GetSize(); ++iter)
		{
			m_fileCreator.Write(m_rows.GetAt(iter), m_rows.GetAt(iter).GetLength());
		}
		m_rows.RemoveAll();
		m_commit = true;
	}
	catch (CFileException error)
	{
		DWORD dwErr = GetLastError();
		char errstr[128];
		error.GetErrorMessage(errstr , 128);
		::MessageBox(NULL, errstr,  "CSV writing error", MB_OK);
		return false;
	}

	return true;
}

bool CCSV_Generate::RollBack()
{
	return true;
}