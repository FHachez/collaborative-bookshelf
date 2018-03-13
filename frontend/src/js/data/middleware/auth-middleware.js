import isNil from 'lodash/isNil';
import get from 'lodash/get';
import ls from 'store';
import jwtDecode from 'jwt-decode';
import { CALL_API } from 'redux-api-middleware';

import { LOGOUT } from '../../scenes/login/data/action-types';

export default () => next => (action) => {
  const callApi = action[CALL_API];

  // Check if this action is a redux-api-middleware action.
  if (callApi) {
    // Prepend API base URL to endpoint if it does not already contain a valid base URL.
    if (!/^((http|https|ftp):\/\/)/i.test(callApi.endpoint)) {
      callApi.endpoint = `http://localhost:8081${callApi.endpoint}`;
    }

    // Set headers to empty object if undefined.
    if (isNil(callApi.headers)) {
      callApi.headers = {};
    }

    if (callApi.meta && callApi.meta.customerHeaders) {
      callApi.meta.customerHeaders.foreach((header) => {
        callApi.headers[header.header] = header.value;
      });
    }

    // Set Content-Type to application/json if Content-Type does not already have a value.
    if (isNil(get(callApi.headers, 'Content-Type', null))) {
      callApi.headers['Content-Type'] = 'application/json';
    }

    if (callApi.meta && callApi.meta.noAuth) {
      delete callApi.meta;
      return next(action);
    }

    delete callApi.meta;

    // Check for expiry
    const jwtToken = ls.get('jwt_token') || '';

    if (jwtToken) {
      const decodedJwtToken = jwtDecode(jwtToken);

      if (decodedJwtToken.exp < Date.now() / 1000) {
        const logoutAction = { type: LOGOUT };
        return next(logoutAction);
      }
    } else {
      return next(action);
    }

    callApi.headers.authorization = jwtToken;
  }

  // Pass the FSA to the next action.
  return next(action);
};
