import ls from 'store';

import { LOGIN_SUCCESS, LOGIN_FAILED, LOGOUT, LOGIN_REQUEST, LOGOUT_REQUEST } from './action-types';

export default function loginReducer(state = {}, action) {
  switch (action.type) {
    case LOGIN_SUCCESS: {
      const jwt = action.payload.jwt;
      const name = action.meta.name;

      ls.set('jwt_token', jwt);

      return {
        name,
        jwt,
        attempts: 0,
        loading: false,
      };
    }

    case LOGIN_FAILED: {
      if (state && state.attempts) {
        const nextAttempts = state.attempts + 1;
        return {
          ...state,
          attempts: nextAttempts,
        };
      }

      return {
        name: '',
        jwt: '',
        attempts: 1,
        loading: false,
      };
    }

    case LOGIN_REQUEST: {
      return {
        ...state,
        loading: true,
      };
    }

    case LOGOUT_REQUEST: {
      if (action.error) {
        ls.remove('jwt_token');
        return {};
      }

      return {
        ...state,
        loading: true,
      };
    }

    case LOGOUT: {
      ls.remove('jwt_token');

      return {};
    }

    default:
      return state;
  }
}
