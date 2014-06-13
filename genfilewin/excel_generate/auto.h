/************************************************************************/
/* auto free memory                                                                     */
/************************************************************************/
#ifndef AUTO_H_
#define AUTO_H_

template <class Type>
class AutoPtr
{
public:
	AutoPtr(Type* pointer):m_pointer(pointer){}
	/*const*/ Type* GetPtr()
	{
		return m_pointer;
	}
	Type& GetRefernce()
	{
		return *m_pointer;
	}
	void Reset(Type* pointer)
	{
		if (m_pointer)
		{
			delete m_pointer;
		}
		m_pointer = pointer;
	}
	~AutoPtr()
	{
		if (m_pointer)
		{
			delete m_pointer;
		}
		m_pointer = NULL;
	}
private:
	Type* m_pointer;
};

#endif