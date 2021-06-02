from django.http import response
from django.shortcuts import render

from dicttoxml import dicttoxml
from lxml import etree
from signxml import XMLSigner

from rest_framework.decorators import api_view
from rest_framework.response import Response

@api_view(('GET',))
def request_access(request):
    data = {
        'device_id' : request.GET['device-id'],
        'location' : request.GET['location'],
        'duration' : request.GET['duration'],
        'time' : request.GET['time']
    }
    xml = dicttoxml(data)
    xml = etree.fromstring(xml)
    cert = open("ds.pem").read()
    key = open("ds.key").read()
    signed_root = XMLSigner().sign(xml, key=key, cert=cert)
    result = etree.tostring(signed_root).decode("utf-8")
    res =  Response(result)
    # res['Content-Disposition'] = "attachmnet; filename=signature.xml"
    return res
