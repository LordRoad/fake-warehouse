/************************************************************************/
/* abtract interface for file generation                                            */
/************************************************************************/
#ifndef FILE_GENERATE_H
#define FILE_GENERATE_H

#include <iostream>
#include <vector>

enum DATA_TYPE
{
	DSTRING = 1,
	DINT,
	DDOUBLE,
	DDATE
};

class CFileGenFactory
{
public:

	/* set some paras for file generation */
	virtual bool InitFile(PVOID paras) = 0;

	/* add table header */
	virtual bool AddHeaders(CStringArray &FieldNames, std::vector<int>* pDatatype  = NULL, bool replace = false) = 0;

	/* add a row into table */
	virtual bool AddRow(CStringArray &RowValues, long row = 0, bool replace = false) = 0;
	virtual bool AddRows(CStringArray &RowValues, long len, long beginAt = -1, bool replace = false)  = 0;

	/* will not execute SQL until Commit */
	virtual void Close() = 0;
	virtual void BeginTransaction() = 0;	
	virtual bool Commit() = 0; 
	virtual bool RollBack() = 0;


	//bool FileGenerate() = 0;
	
protected:
private:
};

#endif