import React from 'react';
import { Note } from '../types';
import { formatLocalDate } from '../utils/dateUtils';

interface NoteViewModalProps {
  note: Note | null;
  onClose: () => void;
}

const NoteViewModal: React.FC<NoteViewModalProps> = ({ note, onClose }) => {
  if (!note) return null;

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h3>{note.title}</h3>
        </div>
        <div className="modal-body two-columns">
          <div className="modal-column content-column">
            <strong>Content</strong>
            <div className="content-scroll">
              <p>{note.content}</p>
            </div>
          </div>
          <div className="modal-column metadata-column">
            <div className="metadata-item">
              <strong>Categories:</strong>
              <p>{note.categories.map(c => c.name).join(', ') || 'None'}</p>
            </div>
            <div className="metadata-item">
              <strong>Status:</strong>
              <p>{note.archived ? 'Archived' : 'Active'}</p>
            </div>
            <div className="metadata-item">
              <strong>Created:</strong>
              <p>{formatLocalDate(note.createdAt)}</p>
            </div>
            <div className="metadata-item">
              <strong>Last updated:</strong>
              <p>{formatLocalDate(note.updatedAt)}</p>
            </div>
          </div>
        </div>
        <div className="modal-footer">
          <button className="secondary" onClick={onClose}>Close</button>
        </div>
      </div>
    </div>
  );
};

export default NoteViewModal;
