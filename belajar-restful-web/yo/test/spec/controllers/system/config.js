'use strict';

describe('Controller: SystemConfigCtrl', function () {

  // load the controller's module
  beforeEach(module('yoApp'));

  var SystemConfigCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    SystemConfigCtrl = $controller('SystemConfigCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
