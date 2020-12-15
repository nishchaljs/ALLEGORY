import os
import ibmdb2

def plot():
    conn_str="DATABASE=BLUDB;HOSTNAME=dashdb-txn-sbox-yp-lon02-07.services.eu-gb.bluemix.net;PORT=50000;PROTOCOL=TCPIP;UID=ttm65995;PWD=nmvs48nv5b@8mp6h;"
    ibm_db_conn = ibmdb2.db(conn_str)
    cols = ibm_db_conn.read('select * from ADMIN')
    return cols

