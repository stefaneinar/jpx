#include <stdio.h>
#include <string.h>

#include "SWI-Prolog.h"
#include "JPX.h"

static jobject lookup(JNIEnv *env, term_t term) {
	jobject termObject;

	jclass termClass     = (*env)->FindClass(env,"Ljpx/impl/TermImpl;");
	jclass variableClass = (*env)->FindClass(env,"Ljpx/impl/VariableImpl;");
	jclass integerClass  = (*env)->FindClass(env,"Ljpx/impl/IntegerImpl;");
	jclass atomClass     = (*env)->FindClass(env,"Ljpx/impl/AtomImpl;");
	jclass compoundClass = (*env)->FindClass(env,"Ljpx/impl/CompoundImpl;");

	jfieldID fid = (*env)->GetFieldID(env, termClass, "id", "J");

	switch( PL_term_type(term) ) {
	case PL_VARIABLE:
		termObject = (*env)->AllocObject(env, variableClass);
		break;
	case PL_ATOM:
		termObject = (*env)->AllocObject(env, atomClass);
		break;
	case PL_INTEGER:
		termObject = (*env)->AllocObject(env, integerClass);
		break;
	case PL_TERM:
		termObject = (*env)->AllocObject(env, compoundClass);
		break;
	default:
		return NULL;
		break;
	}

	(*env)->SetLongField(env, termObject, fid, term);

	return termObject;
}

/* Fyrir: bindings := ['key1'=value1,...,'keyN'=valueN]
 * Eftir: 'out' geymir value sem svarar til 'key'
 *        og skilar TRUE annars out �skilgreint og skilar FALSE
 * */
static int find_in_bindings(term_t bindings, const char* key, term_t out) {
	char* buffer;
	term_t head  = PL_new_term_ref();
	term_t item  = PL_new_term_ref();
	term_t value = PL_new_term_ref();

	functor_t func = PL_new_functor(PL_new_atom("="), 2);

	term_t list = PL_copy_term_ref(bindings);

	while( PL_get_list(list, head, list) ) {
		if( PL_is_functor(head, func) ) {
			PL_get_arg(1, head, item);
			PL_get_arg(2, head, value);

			if( PL_is_atom(item) ) {
				PL_get_atom_chars(item, &buffer);
				if( strcmp(buffer, key) == 0 ) {
					PL_unify(out,value);
					return 1;
				}
			}
		}
	}

	return 0;
}


/*****************************************
 *
 *   AtomImpl
 *
 *****************************************/

JNIEXPORT jstring JNICALL Java_jpx_impl_AtomImpl_nameImpl
  (JNIEnv *env, jobject this) {
	jclass atomClass = (*env)->FindClass(env, "Ljpx/impl/AtomImpl;");
	jfieldID fieldID = (*env)->GetFieldID(env, atomClass, "id", "J");

	term_t term = (term_t)(*env)->GetLongField(env, this, fieldID);
	jstring str;

	char* buffer;

	PL_get_atom_chars(term, &buffer);

	str = (*env)->NewStringUTF(env, buffer);

	return str;
}


/*****************************************
 *
 *   IntegerImpl
 *
 *****************************************/

JNIEXPORT jint JNICALL Java_jpx_impl_IntegerImpl_intValueImpl
  (JNIEnv *env, jobject this) {
	jclass integerClass = (*env)->FindClass(env, "Ljpx/impl/IntegerImpl;");
	jfieldID fieldID = (*env)->GetFieldID(env, integerClass, "id", "J");

	term_t term = (term_t)(*env)->GetLongField(env, this, fieldID);

	int value;

	PL_get_integer(term, &value);

	return value;
}


/*****************************************
 *
 *   CompoundImpl
 *
 *****************************************/

JNIEXPORT jstring JNICALL Java_jpx_impl_CompoundImpl_nameImpl
  (JNIEnv *env, jobject this) {
	jclass c     = (*env)->FindClass(env, "Ljpx/impl/CompoundImpl;");
	jfieldID fid = (*env)->GetFieldID(env, c, "id", "J");
	term_t term  = (term_t)(*env)->GetLongField(env, this, fid);

	atom_t str;
	int arity;

	PL_get_name_arity(term, &str, &arity);

	return (*env)->NewStringUTF(env, PL_atom_chars(str));
}

JNIEXPORT jint JNICALL Java_jpx_impl_CompoundImpl_arityImpl
  (JNIEnv *env, jobject this) {
	jclass termClass = (*env)->FindClass(env, "Ljpx/impl/CompoundImpl;");
	jfieldID fid = (*env)->GetFieldID(env, termClass, "id", "J");
	term_t term = (term_t)(*env)->GetLongField(env, this, fid);

	atom_t name;
	int    arity;
	PL_get_name_arity(term, &name, &arity);

	return arity;
}


JNIEXPORT jobject JNICALL Java_jpx_impl_CompoundImpl_termImpl
  (JNIEnv *env, jobject this, jint index) {
	jclass termClass = (*env)->FindClass(env, "Ljpx/impl/TermImpl;");

	jfieldID fid = (*env)->GetFieldID(env, termClass, "id", "J");
	term_t term   = (term_t)(*env)->GetLongField(env, this, fid);

	term_t item = PL_new_term_ref();
	PL_get_arg(index+1, term, item);

	return lookup(env, item);
}


/*****************************************
 *
 *   JPX
 *
 *****************************************/

JNIEXPORT void JNICALL Java_jpx_JPX_init
  (JNIEnv *env, jobject this) {
	static char* args[] = {"libjpx.dylib", "-nosignals", "-q", "-L16m", "-G16m", NULL};

	if( !PL_initialise(4, args) ) {
		PL_halt(1);
	}
}

JNIEXPORT void JNICALL Java_jpx_JPX_initWithSavedState
  (JNIEnv *env, jobject this, jstring savedState) {
	static char* args[] = {"libjpx.so", "-q", "-x", NULL, NULL};

	const char *buffer = (*env)->GetStringUTFChars(env, savedState, NULL);

	args[3] = (char*)buffer;

	if( !PL_initialise(4, args) ) {
		PL_halt(1);
	}

	(*env)->ReleaseStringUTFChars(env, savedState, buffer);
}

JNIEXPORT void JNICALL Java_jpx_JPX_release
  (JNIEnv *env, jobject this) {
	PL_cleanup(0);
}

JNIEXPORT jlong JNICALL Java_jpx_JPX_version
  (JNIEnv *env, jobject this) {
	jlong version = PL_query( PL_QUERY_VERSION );
	return version;
}

JNIEXPORT jobject JNICALL Java_jpx_JPX_query
  (JNIEnv *env, jobject this, jbyteArray query) {
	jclass queryClass  = (*env)->FindClass(env, "Ljpx/impl/QueryImpl;");
	jfieldID idFieldId = (*env)->GetFieldID(env, queryClass, "id", "J");
	jfieldID biFieldId = (*env)->GetFieldID(env, queryClass, "bindings", "J");
	qid_t qid;

	predicate_t attPred  = PL_predicate("atom_to_term", 3, NULL);
	predicate_t callPred = PL_predicate("call", 1, NULL);

	term_t params = PL_new_term_refs(3);

	jsize size = (*env)->GetArrayLength(env, query);
	jbyte *buffer = (*env)->GetByteArrayElements(env, query, NULL);

	jobject queryObj;

	PL_unify_atom_nchars(params, size, (const char *)buffer);

	qid = PL_open_query(NULL, PL_Q_NORMAL, attPred, params);
	if( PL_next_solution(qid) )
	{
/*
		if( PL_exception(qid) ) {
			printf("An JPX exception should be thrown [Java_jpx_JPX_query].\n");
			return 0;
		}
*/
	}
	PL_cut_query( qid );

	qid = PL_open_query(NULL, PL_Q_NORMAL, callPred, params+1);

	queryObj = (*env)->AllocObject(env, queryClass);
	(*env)->SetLongField(env, queryObj, idFieldId, qid);
	(*env)->SetLongField(env, queryObj, biFieldId, params+2);

	return queryObj;
}


/*****************************************
 *
 *   Term
 *
 *****************************************/
JNIEXPORT jboolean JNICALL Java_jpx_impl_TermImpl_isCompound
  (JNIEnv *env, jobject this) {
	jclass termClass = (*env)->FindClass(env, "Ljpx/impl/TermImpl;");
	jfieldID fid = (*env)->GetFieldID(env, termClass, "id", "J");
	term_t term   = (term_t)(*env)->GetLongField(env, this, fid);
	return PL_is_compound(term) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_jpx_impl_TermImpl_isAtom
  (JNIEnv *env, jobject this) {
	jclass termClass = (*env)->FindClass(env, "Ljpx/impl/TermImpl;");
	jfieldID fid = (*env)->GetFieldID(env, termClass, "id", "J");
	term_t term = (term_t)(*env)->GetLongField(env, this, fid);
	return PL_is_atom(term) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_jpx_impl_TermImpl_isVariable
  (JNIEnv *env, jobject this) {
	jclass termClass = (*env)->FindClass(env, "Ljpx/impl/TermImpl;");
	jfieldID fid = (*env)->GetFieldID(env, termClass, "id", "J");
	term_t term = (term_t)(*env)->GetLongField(env, this, fid);
	return PL_is_variable(term) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jstring JNICALL Java_jpx_impl_TermImpl_termToString
  (JNIEnv *env, jobject this) {
	char *buffer;
	jstring result;
	predicate_t pred;
	qid_t qid;

	jclass termClass = (*env)->FindClass(env, "Ljpx/impl/TermImpl;");
	jfieldID termFieldID = (*env)->GetFieldID(env, termClass, "id", "J");

	term_t term = (term_t)(*env)->GetLongField(env, this, termFieldID);
	term_t params = PL_new_term_refs(2);
	PL_unify(params,term);

	pred = PL_predicate("term_to_atom", 2, NULL);

	qid = PL_open_query(NULL, PL_Q_NORMAL, pred, params);
	PL_next_solution(qid);

	PL_get_atom_chars(params+1, &buffer);

	result = (*env)->NewStringUTF(env, buffer);

	PL_close_query(qid);

	return result;
}


/*****************************************
 *
 *   Query
 *
 *****************************************/
JNIEXPORT jboolean JNICALL Java_jpx_impl_QueryImpl_next
  (JNIEnv *env, jobject this) {
	jclass queryClass  = (*env)->FindClass(env, "Ljpx/impl/QueryImpl;");
	jfieldID idFieldID = (*env)->GetFieldID(env, queryClass, "id", "J");

	int result;

	qid_t qid = (qid_t)(*env)->GetLongField(env, this, idFieldID);
	result = PL_next_solution(qid);
/*
	if( PL_exception(qid) ) {
		printf("An JPX exception should be thrown [Java_jpx_impl_Query_Impl_next].\n");
		return 0;
	}
*/

	return result;
}

JNIEXPORT void JNICALL Java_jpx_impl_QueryImpl_cut
  (JNIEnv *env, jobject this) {
	jclass queryClass  = (*env)->FindClass(env, "Ljpx/impl/QueryImpl;");
	jfieldID idFieldID = (*env)->GetFieldID(env, queryClass, "id", "J");

	qid_t qid = (qid_t)(*env)->GetLongField(env, this, idFieldID);

	PL_cut_query(qid);
}

JNIEXPORT void JNICALL Java_jpx_impl_QueryImpl_close
  (JNIEnv *env, jobject this) {
	jclass queryClass  = (*env)->FindClass(env, "Ljpx/impl/QueryImpl;");
	jfieldID idFieldID = (*env)->GetFieldID(env, queryClass, "id", "J");

	qid_t qid = (qid_t)(*env)->GetLongField(env, this, idFieldID);

	PL_close_query(qid);
}

JNIEXPORT jobject JNICALL Java_jpx_impl_QueryImpl_getImpl
  (JNIEnv *env, jobject this, jstring key) {
	jclass queryClass    =(*env)->FindClass(env,"Ljpx/impl/QueryImpl;");

	jfieldID biFieldId=(*env)->GetFieldID(env,queryClass,"bindings","J");

	const char* buffer = (*env)->GetStringUTFChars(env, key, NULL);

	jlong bi = (*env)->GetLongField(env, this, biFieldId);

	term_t term = PL_new_term_ref();

	jobject termObj = NULL;
	if( find_in_bindings( (term_t)bi, buffer, term) ) {
		termObj = lookup(env, term);
	}

	(*env)->ReleaseStringUTFChars(env, key, buffer);

	return termObj;
}
