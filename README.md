lift-isnippet
=============

This is an extend of lift framework. You can image it as an intelligent lazy load snippet.
It is a mix of parallel snippet and lazy load snippet.So you can call it as a intelligent lazy load snippet.

We know there is a timeout limit with parallel snippet, which cause a error output to the page if the execution of snippet become timeout. This extension will intelligently decide to render the manufactured html if the manufacturing finished in time or to output a lazy load js hanlder if the manufacturing did not finished in time.