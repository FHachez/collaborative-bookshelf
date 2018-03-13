import TOGGLE_NAVBAR from './action-types';

export default function clientsReducer(state = { open: false }, action) {
  switch (action.type) {
    case TOGGLE_NAVBAR: {
      return {
        open: !state.open
      };
    }

    default:
      return state;
  }
}
