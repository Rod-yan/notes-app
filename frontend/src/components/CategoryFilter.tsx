import React from 'react';
import { Category } from '../types';

interface CategoryFilterProps {
    categories: Category[];
    selectedCategory: string;
    onCategoryChange: (category: string) => void;
}

const CategoryFilter: React.FC<CategoryFilterProps> = ({ categories, selectedCategory, onCategoryChange }) => {
    return (
        <div style={{ marginBottom: '10px' }}>
            <label>Filter by Category: </label>
            <select 
                value={selectedCategory} 
                onChange={(e) => onCategoryChange(e.target.value)}
            >
                <option value="">All</option>
                {categories.map(category => (
                    <option key={category.id} value={category.name}>
                        {category.name}
                    </option>
                ))}
            </select>
        </div>
    );
};

export default CategoryFilter;
