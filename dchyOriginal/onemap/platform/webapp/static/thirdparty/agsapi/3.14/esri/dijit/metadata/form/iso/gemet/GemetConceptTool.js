// All material copyright ESRI, All Rights Reserved, unless otherwise specified.
// See http://js.arcgis.com/3.14/esri/copyright.txt for details.
//>>built
define("esri/dijit/metadata/form/iso/gemet/GemetConceptTool","dojo/_base/declare dojo/_base/lang dojo/has ../../tools/ClickableTool ../../../base/etc/docUtil ./ConceptDialog ../../../../../kernel".split(" "),function(a,d,c,e,f,g,h){a=a([e],{postCreate:function(){this.inherited(arguments)},startup:function(){if(!this._started){var a=f.findGxeContext(this);if(!a||!a.gemetUrl||!a.gemetConceptThesaurus)this.domNode.style.display="none"}},whenToolClicked:function(a,b){if(b&&b.parentXNode){var c=b.getInputValue();
(new g({gxeDocument:b.parentXNode.gxeDocument,initialValue:c,onSelect:d.hitch(this,function(a){b.setInputValue(a)})})).show()}}});c("extend-esri")&&d.setObject("dijit.metadata.form.iso.gemet.GemetConceptTool",a,h);return a});