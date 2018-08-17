/**
 * Created by GiangDD on 3/26/2017.
 */
'use strict';
angular.module('jsClientApp').factory("smppParametersService", function ($http, $rootScope, CONFIG, cF) {

  //service for get config SMPP
  var obj = {};
  obj.getConfigSmpp = function () {
    var url = CONFIG.SERVICE_BASE + 'smpp-paramaters/get-config';
    return $http.get(url);
  };

  //service for update bill
  obj.saveConfigSmpp = function (object) {
    //var data =  encodeURIComponent(JSON.stringify(object));
    return $http.post(CONFIG.SERVICE_BASE + 'smpp-paramaters/save-config', object);
  };

  return obj;
});
