var init = function(views) {
 var deployApp = function(viewconf) {
   deployJava.runApplet(
    {code:'jflowmap.JFlowMapApplet', width:'100%', height:'100%', archive:'jflowmap.jar'},
    {viewConfig:'viewconf/'+viewconf+'.jfmv', codebase_lookup:'.',
     image:'resources/loading.gif', boxbgcolor:'white', boxborder:'false', centerimage:'true'},
    '1.6');
 };
 var writeViewLinks = function(view) {
  var i, view, viewconf;
  document.write('<h3 class="view">'+view.name+' view</h3>');
  if (view.hasOwnProperty('desc')) {
    document.write('<p class="viewdesc">'+view.desc+'</p>');
  }
  document.write('<ul class="views">');
  for (i=0;i<view.viewconfs.length;i++) {    
    viewconf = view.viewconfs[i];
    document.write('<li><a href="?'+viewconf.jfmv+'">'+viewconf.name+'</a>');
    if (viewconf.hasOwnProperty('desc')) {
      document.write('<p class="jfvmdesc">&ndash; '+viewconf.desc+'</p>');
    }
  }
  document.write('</ul>');
 };
 var i, view;
 var viewconf = location.search.substr(1);
 if (viewconf) {
  deployApp(viewconf);
 } else {
  document.write('<div style="margin:10px;"><h1>jflowmap demo applets</h1>');
  for (i=0;i<views.length;i++) { writeViewLinks(views[i]); };
  document.write('</div>');
 }
};
