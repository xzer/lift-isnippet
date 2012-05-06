lift-isnippet
=============

This is an extend of lift framework. You can image it as an intelligent lazy load snippet.
It is a mix of parallel snippet and lazy load snippet.So you can call it as a intelligent lazy load snippet.

We know there is a timeout limit with parallel snippet, which cause a error output to the page if the execution of snippet become timeout. This extension will intelligently decide to render the manufactured html if the manufacturing finished in time or to output a lazy load js hanlder if the manufacturing did not finished in time.

See the following example:

>      <div class="lift:ISnippetExample.renderAfterSleep?sleep=1"></div>
>      <div class="lift:ISnippetExample.renderAfterSleep?sleep=2"></div>
>      <div class="lift:ISnippetExample.renderAfterSleep?sleep=3"></div>
>      <div class="lift:ISnippetExample.renderAfterSleep?sleep=4"></div>
>      <div class="lift:ISnippetExample.renderAfterSleep?sleep=5"></div>
>      <div class="lift:ISnippetExample.renderAfterSleep?sleep=6"></div>
>      <div class="lift:ISnippetExample.renderAfterSleep?sleep=7"></div>
>      <div class="lift:ISnippetExample.renderAfterSleep?sleep=8"></div>
>      <div class="lift:ISnippetExample.renderAfterSleep?sleep=9"></div>

There are 8 snippet output requests and each one would sleep some seconds specified by the parameter "sleep", so this page would take about 36 seconds. 

Usually we want to complete the page rendering in a small total time, so there is an option which is parallel snippet of lift. However there is a timeout limit in parallel snippet, which is necessary because lift will waiting for all the parallel snippets finish their rendering and combine them as the output stream for the client and if there were no a timeout, the client request might be hung up by a potential long time snippet. Even we disregard the timeout, there is also a situation that parallel snippet cannot work perfectly for us: as the above example, if we use parallel snippet, the page would be rendered in about 8 seconds which is a reasonable time for some certain operation but we still don't want to output a page after 8 seconds from client send a request, which is too long to an user. So assume that we want the page rendering time could be limited to 5 seconds, how should we do?

There is also an existed option which is lazy load snippet of lift.A lazy load snippet would output a javascript comet handler immediately instead of waiting for the snippet finishing its execution, which is not bad but we want to have a smarter way to decide what should be output to client, a javascript comet handler or a piece of manufactured html codes.

See the following source

>        <div id="dummy" class="lift:ISnippet?parallel=true">
>           <div class="lift:ISnippetExample.renderAfterSleep?sleep=1"></div>
>        </div>
>         
>        <div id="dummy" class="lift:ISnippet?parallel=true">
>           <div class="lift:ISnippetExample.renderAfterSleep?sleep=2"></div>
>        </div>
>        
>        <div id="dummy" class="lift:ISnippet?parallel=true">
>           <div class="lift:ISnippetExample.renderAfterSleep?sleep=3"></div>
>        </div>
>         
>        <div id="dummy" class="lift:ISnippet?parallel=true">
>           <div class="lift:ISnippetExample.renderAfterSleep?sleep=4"></div>
>        </div>
>        
>        <div id="dummy" class="lift:ISnippet?parallel=true">
>           <div class="lift:ISnippetExample.renderAfterSleep?sleep=5"></div>
>        </div>
>         
>        <div id="dummy" class="lift:ISnippet?parallel=true">
>           <div class="lift:ISnippetExample.renderAfterSleep?sleep=6"></div>
>        </div>
>        
>        <div id="dummy" class="lift:ISnippet?parallel=true">
>           <div class="lift:ISnippetExample.renderAfterSleep?sleep=7"></div>
>        </div>
>         
>        <div id="dummy" class="lift:ISnippet?parallel=true">
>           <div class="lift:ISnippetExample.renderAfterSleep?sleep=8"></div>
>        </div>


We wrapped the snippets by our intelligent lazy load snippet named "ISnippet", what would happen? Since the default wait time is 5 seconds, so the page will be output to client after 5 seconds, and the first 4 snippets will be output as pure html codes and the following 4 snippets will be output as javascript comet hanlder instead of html codes, then after the page is rendered to client, the user will see the left snippets being replaced by the real result one by one in 1 seconds interval.