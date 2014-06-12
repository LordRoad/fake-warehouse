/************************************************************************/
/* CSV file generate                                                                      */
/************************************************************************/
#ifndef CSV_GENERATE_H_
#define CSV_GENERATE_H_

#include "file_generate.h"

/*
 import is used to export lib files from COM and generate into Debug or Release directory
 such as excel.tlh, mso.lh, vbetext.olb
*/
#ifdef COM_EXCEL_GEN

#import "C:\\Program Files (x86)\\Common Files\\microsoft shared\\OFFICE14\\MSO.DLL" \
	rename("RGB","MsoRGB") \
	rename("SearchPath","MsoSearchPath")

#import "C:\\Program Files (x86)\\Common Files\\microsoft shared\\VBA\\VBA6\\VBE6EXT.OLB"

using namespace VBIDE;

#import "C:\\Program Files (x86)\\Microsoft Office\\Office14\\EXCEL.EXE" \
	rename( "DialogBox", "ExcelDialogBox" ) \
	rename( "RGB", "ExcelRGB" ) \
	rename( "CopyFile", "ExcelCopyFile" ) \
	rename( "ReplaceText", "ExcelReplaceText" ) \
	exclude( "IFont", "IPicture" ) no_dual_interfaces

using namespace Office;

#endif

class CCSV_Generate : public CFileGenFactory
{
public:
	CCSV_Generate(CString strFileName);
	~CCSV_Generate();
	
	/* set some paras for file generation */
	bool InitFile(PVOID paras);

	/* add table header */
	bool AddHeaders(CStringArray &FieldNames, std::vector<int>* pDatatype  = NULL, bool replace = false);

	/* add a row into table */
	bool AddRow(CStringArray &RowValues, long row = 0, bool replace = false);
	bool AddRows(CStringArray &RowValues, long len, long beginAt = -1, bool replace = false);

	/* will not execute SQL until Commit */
	void Close();
	/*
	* as for transaction, i should save the rows first and then reopen the file, continuing writing.
	* but here, take it as simple as possible and just save it after user calls API 'close'
	*/
	void BeginTransaction();	
	bool Commit(); 
	bool RollBack();

protected:
	bool begin_csv(CString strName);
	void end_csv();
private:
	long m_curRow;
	long m_totalRow;
	bool m_commit;
	bool m_status;
	CString m_filename;
	CFile m_fileCreator;
	std::vector<int> m_datatype;

	CStringArray m_rows;

	/*Excel::_ApplicationPtr m_pExcelApp;
	Excel::_WorkbookPtr m_pWorkbook;
	Excel::_WorksheetPtr m_pWorksheet;
	Excel::RangePtr m_pRange;*/

};

#endif